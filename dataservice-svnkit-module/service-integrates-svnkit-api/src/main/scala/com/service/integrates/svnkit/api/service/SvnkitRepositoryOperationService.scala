package com.service.integrates.svnkit.api.service

import java.io.File
import java.util

import com.service.integrates.svnkit.api.ServiceSvnkitConfigurationProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.tmatesoft.svn.core.{SVNException, SVNURL}
import org.tmatesoft.svn.core.io.{ISVNSession, SVNRepository, SVNRepositoryFactory}

import scala.collection.JavaConversions._

trait SvnkitRepositoryOperationService {

  @Autowired
  val groupService: SvnkitGroupOperationService = null

  @Autowired
  val property: ServiceSvnkitConfigurationProperties = null

  @throws[SVNException]
  def create(url: SVNURL): SVNRepository = {
    SVNRepositoryFactory.create(url)
  }

  @throws[SVNException]
  def create(url: SVNURL, options: ISVNSession): SVNRepository = {
    SVNRepositoryFactory.create(url, options)
  }

  //  @throws[SVNException]
  //  def createLocalRepository(path: File, uuid: String, enableRevisionProperties: Boolean, force: Boolean): SVNURL = {
  //    SVNRepositoryFactory.createLocalRepository(path, uuid, enableRevisionProperties, force)
  //  }
  //
  //  @throws[SVNException]
  //  def createLocalRepository(path: File, uuid: String, enableRevisionProperties: Boolean, force: Boolean, pre14Compatible: Boolean): SVNURL = {
  //    SVNRepositoryFactory.createLocalRepository(path, uuid, enableRevisionProperties, force, pre14Compatible)
  //  }
  //
  //  @throws[SVNException]
  //  def createLocalRepository(path: File, uuid: String, enableRevisionProperties: Boolean, force: Boolean, pre14Compatible: Boolean, pre15Compatible: Boolean): SVNURL = {
  //    SVNRepositoryFactory.createLocalRepository(path, uuid, enableRevisionProperties, force, pre14Compatible, pre15Compatible)
  //  }
  //
  //  @throws[SVNException]
  //  def createLocalRepository(path: File, uuid: String, enableRevisionProperties: Boolean, force: Boolean, pre14Compatible: Boolean, pre15Compatible: Boolean, pre16Compatible: Boolean): SVNURL = {
  //    SVNRepositoryFactory.createLocalRepository(path, uuid, enableRevisionProperties, force, pre14Compatible, pre15Compatible, pre16Compatible)
  //  }
  //
  //  @throws[SVNException]
  //  def createLocalRepository(path: File, uuid: String, enableRevisionProperties: Boolean, force: Boolean, pre14Compatible: Boolean, pre15Compatible: Boolean, pre16Compatible: Boolean, pre17Compatible: Boolean, with17Compatible: Boolean): SVNURL = {
  //    SVNRepositoryFactory.createLocalRepository(path, uuid, enableRevisionProperties, force, pre14Compatible, pre15Compatible, pre16Compatible, pre17Compatible, with17Compatible)
  //  }

  /**
    * 创建本地仓库并授权
    *
    * @param path
    * @param enableRevisionProperties
    * @param force
    * @param values
    * @throws
    * @return
    */
  @throws[SVNException]
  def createLocalRepository(path: File, enableRevisionProperties: Boolean, force: Boolean, values: util.Map[String, String]): SVNURL = {
    // 创建仓库
    val url = createLocalRepository(path, enableRevisionProperties, force)
    // 添加权限
    addRights(path.getName, "/", values)
    url
  }

  /**
    * 异步创建本地仓库
    *
    * @param path
    * @param enableRevisionProperties
    * @param force
    * @throws
    */
  @Async(value = "ServiceExecutor")
  @throws[SVNException]
  def asyncCreateLocalRepository(path: File, enableRevisionProperties: Boolean, force: Boolean): Unit = {
    try {
      // 创建仓库
      createLocalRepository(path, enableRevisionProperties, force)
    } catch {
      case _: Throwable => /* ignore */
    }
  }

  /**
    * 异步创建本地仓库并授权
    *
    * @param path
    * @param enableRevisionProperties
    * @param force
    * @param values
    * @throws
    */
  @Async(value = "ServiceExecutor")
  @throws[SVNException]
  def asyncCreateLocalRepository(path: File, enableRevisionProperties: Boolean, force: Boolean, values: util.Map[String, String]): Unit = {
    try {
      // 创建仓库
      createLocalRepository(path, enableRevisionProperties, force, values)
    } catch {
      case _: Throwable => /* ignore */
    }
  }

  /**
    * 创建本地仓库
    *
    * @param repositoryName
    * @param enableRevisionProperties
    * @param force
    * @throws
    */
  @throws[SVNException]
  def createLocalRepository(repositoryName: String, enableRevisionProperties: Boolean, force: Boolean): Unit = {
    createLocalRepository(new File(property.getPath, repositoryName), enableRevisionProperties, force)
  }

  /**
    * 创建本地仓库并授权
    *
    * @param repositoryName
    * @param enableRevisionProperties
    * @param force
    * @param values
    * @throws
    */
  @throws[SVNException]
  def createLocalRepository(repositoryName: String, enableRevisionProperties: Boolean, force: Boolean, values: util.Map[String, String]): Unit = {
    createLocalRepository(new File(property.getPath, repositoryName), enableRevisionProperties, force, values)
  }

  /**
    * 批量创建本地仓库
    *
    * @param repositoryNames
    * @param enableRevisionProperties
    * @param force
    * @throws
    */
  @throws[SVNException]
  def createLocalRepositoryBatch(repositoryNames: util.List[String], enableRevisionProperties: Boolean, force: Boolean): Unit = {
    // 并发
    repositoryNames.par.foreach(repositoryName => {
      // 异步
      asyncCreateLocalRepository(new File(property.getPath, repositoryName), enableRevisionProperties, force)
    })
  }

  /**
    * 批量创建本地仓库并授权
    *
    * @param repositoryNames
    * @param enableRevisionProperties
    * @param force
    * @param values
    * @throws
    */
  @throws[SVNException]
  def createLocalRepositoryBatch(repositoryNames: util.List[String], enableRevisionProperties: Boolean, force: Boolean, values: util.Map[String, String]): Unit = {
    // 并发
    repositoryNames.par.foreach(repositoryName => {
      // 异步
      asyncCreateLocalRepository(new File(property.getPath, repositoryName), enableRevisionProperties, force, values)
    })
  }

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
  def createLocalRepository(path: File, enableRevisionProperties: Boolean, force: Boolean): SVNURL

  /**
    * 列出条目权限
    *
    * @param repositoryName 仓库名称
    * @param repositoryPath 条目路径
    * @return
    */
  @throws[Exception]
  def listRights(repositoryName: String, repositoryPath: String): util.Map[String, util.Map[String, String]]

  /**
    * 列出当前仓库所有用户
    *
    * @param repositoryName 仓库名称
    * @return Map(对象 -> 权限)
    */
  @throws[Exception]
  def listRightUsers(repositoryName: String): util.Map[String, String]

  /**
    * 仓库条目授权
    *
    * @param repositoryName 仓库名称
    * @param repositoryPath 条目路径
    * @param values         权限集
    */
  @throws[Exception]
  def addRights(repositoryName: String, repositoryPath: String, values: util.Map[String, String]): Unit

  /**
    * 删除对象权限
    *
    * @param repositoryName 仓库名称
    * @param repositoryPath 条目路径
    * @param names          对象名称
    * @return
    */
  @throws[Exception]
  def deleteRights(repositoryName: String, repositoryPath: String, names: Array[String]): Unit

  /**
    * 列出所有仓库
    *
    * @return
    */
  @throws[Exception]
  def listRepositories(): util.List[String]

  /**
    * 列出用户有权限的仓库
    *
    * @param username 用户名称
    * @return
    */
  @throws[Exception]
  def userHaveRightRepositories(username: String): util.List[String]

  /**
    * 列出组别有权限的仓库
    *
    * @param groupName 组别名称
    * @return
    */
  @throws[Exception]
  def groupHaveRightRepositories(groupName: String): util.List[String]
}
