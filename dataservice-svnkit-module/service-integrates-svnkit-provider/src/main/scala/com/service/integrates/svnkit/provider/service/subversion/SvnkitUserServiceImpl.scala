package com.service.integrates.svnkit.provider.service.subversion

import java.io.File
import java.util

import com.service.integrates.ini4j.service.IniOperationService
import com.service.integrates.svnkit.api.service.SvnkitUserOperationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

import scala.collection.JavaConversions._

@Service
@ConditionalOnProperty(value = Array("service.svnkit.version"), havingValue = "subversion")
class SvnkitUserServiceImpl extends SvnkitUserOperationService {

  @Autowired
  val iniService: IniOperationService = null

  val sectionName = "users"

  def userConfigFile(): File = new File(property.getPath, property.getPasswdFile)

  /**
    * 列出所有用户
    *
    * @return
    */
  override def listUsers(): util.List[String] = {
    iniService.loadConfig(userConfigFile()).get(sectionName).keySet().toList
  }

  /**
    * 创建用户
    *
    * @param username 用户名称
    * @param password 用户密码
    */
  @throws[Exception]
  override def createUser(username: String, password: String): Unit = {
    val ini = iniService.loadConfig(userConfigFile())
    if (ini.keys.contains(sectionName)) {
      val section = ini.get(sectionName)
      if (section.keys.contains(username)) {
        throw new Exception("用户已存在")
      } else {
        iniService.addConfig(userConfigFile(), sectionName, username, password)
      }
    } else {
      iniService.addConfig(userConfigFile(), sectionName, username, password)
    }
  }

  /**
    * 批量创建用户
    *
    * @param users
    */
  override def createUserBatch(users: util.Map[String, String]): Unit = {
    val ini = iniService.loadConfig(userConfigFile())
    if (ini.keys.contains(sectionName)) {
      iniService.addConfigs(userConfigFile(), sectionName, users.filter(user => !ini.get(sectionName).contains(user._1)).map(user => Map(user._1 -> user._2)).fold(Map.empty[String, String])(_ ++ _))
    } else {
      iniService.addConfigs(userConfigFile(), sectionName, users.map(user => Map(user._1 -> user._2)).fold(Map.empty[String, String])(_ ++ _))
    }
  }

  /**
    * 修改用户
    *
    * @param username 用户名称
    * @param password 用户密码
    */
  @throws[Exception]
  override def updateUser(username: String, password: String): Unit = {
    iniService.addConfig(userConfigFile(), sectionName, username, password)
  }
}
