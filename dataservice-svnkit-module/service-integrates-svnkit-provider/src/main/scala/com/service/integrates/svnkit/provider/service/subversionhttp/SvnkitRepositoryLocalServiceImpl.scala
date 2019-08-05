package com.service.integrates.svnkit.provider.service.subversionhttp

import java.io.File
import java.util

import com.service.integrates.ini4j.service.IniOperationService
import com.service.integrates.svnkit.api.service.SvnkitRepositoryOperationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import org.tmatesoft.svn.core.io.SVNRepositoryFactory
import org.tmatesoft.svn.core.{SVNException, SVNURL}

import scala.collection.JavaConversions._

@Service
@ConditionalOnProperty(value = Array("service.svnkit.version"), havingValue = "subversionhttp")
class SvnkitRepositoryLocalServiceImpl extends SvnkitRepositoryOperationService {

  @Autowired
  val iniService: IniOperationService = null

  val sectionName = "general"

  /**
    * 创建本地仓库
    *
    * @param path
    * @param enableRevisionProperties
    * @param force
    * @throws
    * @return
    */
  @throws[SVNException]
  override def createLocalRepository(path: File, enableRevisionProperties: Boolean, force: Boolean): SVNURL = {
    val url = SVNRepositoryFactory.createLocalRepository(path, enableRevisionProperties, force)
    iniService.addConfigs(new File(new File(property.getPath, path.getName), property.getSvnServeConf), sectionName, Map(
      "anon-access" -> "none",
      "auth-access" -> "write",
      "password-db" -> s"${property.getPath}/${property.getPasswdFile}",
      "authz-db" -> s"${property.getPath}/${property.getAuthzFile}"
    ))
    url
  }

  /**
    * 列出条目权限
    *
    * @param repositoryName 仓库名称
    * @param repositoryPath 条目路径
    * @return
    */
  override def listRights(repositoryName: String, repositoryPath: String): util.Map[String, util.Map[String, String]] = {
    // 取得仓库条目的真实路径
    val sectionPaths = repositoryPath.split("/", -1)
    // 保存权限信息
    val map: util.Map[String, util.Map[String, String]] = new util.HashMap[String, util.Map[String, String]]()

    val ini = iniService.loadConfig(new File(property.getPath, property.getAuthzFile))

    (0 until sectionPaths.length).foreach(i => {
      val sectionName = s"${repositoryName}:${if (i == 0) "/" else (0 to i).map(sectionPaths(_)).mkString("/")}"
      if (i == sectionPaths.length - 1) {
        if (ini.keySet().contains(sectionName)) {
          val section = ini.get(sectionName)
          section.keySet().foreach(key => {
            map.put(key, Map(section.get(key) -> "self"))
          })
        }
      } else {
        if (ini.keySet().contains(sectionName)) {
          val section = ini.get(sectionName)
          section.keySet().foreach(key => {
            map.put(key, Map(section.get(key) -> "parent"))
          })
        }
      }
    })

    map
  }

  /**
    * 列出当前仓库所有用户
    *
    * @param repositoryName 仓库名称
    * @return Map(对象 -> 权限)
    */
  override def listRightUsers(repositoryName: String): util.Map[String, String] = {
    // 保存权限信息
    val map: util.Map[String, String] = new util.HashMap[String, String]()
    // 权限控制文件
    val ini = iniService.loadConfig(new File(property.getPath, property.getAuthzFile))

    if (ini.keySet().contains(s"${repositoryName}:/")) {
      // 获取根的权限控制
      val section = ini.get(s"${repositoryName}:/")
      // 权限按照用户所获得的最大权限为准
      // 处理用户权限
      section.keySet().filter(!_.startsWith("@")).foreach(key => {
        map.put(key, section.get(key))
      })
      val allGroups = groupService.loadAllGroups()
      // 处理组别权限
      section.keySet().filter(_.startsWith("@")).foreach(key => {
        val newRight = section.get(key)
        allGroups.toMap.get(key.substring(1)).getOrElse(Array[String]()).foreach(user => {
          val oldRight = map.getOrDefault(user, "")
          map.put(user, if (newRight.length > oldRight.length) newRight else oldRight)
        })
      })
    }

    // 返回结果数据
    map
  }

  /**
    * 仓库条目授权
    *
    * @param repositoryName 仓库名称
    * @param repositoryPath 条目路径
    * @param values         权限集
    */
  override def addRights(repositoryName: String, repositoryPath: String, values: util.Map[String, String]): Unit = {
    iniService.addConfigs(new File(property.getPath, property.getAuthzFile), s"${repositoryName}:${repositoryPath}", values)
  }

  /**
    * 删除对象权限
    *
    * @param repositoryName 仓库名称
    * @param repositoryPath 条目路径
    * @param names          对象名称
    * @return
    */
  override def deleteRights(repositoryName: String, repositoryPath: String, names: Array[String]): Unit = {
    iniService.deleteConfigs(new File(property.getPath, property.getAuthzFile), s"${repositoryName}:${repositoryPath}", names)
  }

  /**
    * 列出所有仓库
    *
    * @return
    */
  override def listRepositories(): util.List[String] = {
    // 仓库根目录
    new File(property.getPath)
      // 根目录下的文件
      .listFiles()
      // 找到包含授权文件的
      .filter(repository => {
      new File(repository, property.getSvnServeConf).exists()
    }).map(_.getName).toList
  }

  private def objectHaveRight(objects: List[String]): List[String] = {
    val ini = iniService.loadConfig(new File(property.getPath, property.getAuthzFile))
    ini.keys
      // 找到仓库的根
      .filter(_.endsWith(":/"))
      // 找到对象有权限访问的根
      .filter(key => ini.get(key).keys.filter(objects.contains(_)).size > 0)
      // 得到仓库名称
      .map(_.split(":", -1)(0))
      // 得到项目名称集合
      .toList
  }

  /**
    * 列出用户有权限的仓库
    *
    * @param username 用户名称
    * @return
    */
  override def userHaveRightRepositories(username: String): util.List[String] = {
    objectHaveRight(groupService.loadAllGroups().filter(_._2.contains(username)).map(group => s"@${group._1}").toList.::(username))
  }

  /**
    * 列出组别有权限的仓库
    *
    * @param groupName 组别名称
    * @return
    */
  override def groupHaveRightRepositories(groupName: String): util.List[String] = {
    objectHaveRight(List(s"@${groupName}"))
  }
}
