package com.service.integrates.svnkit.api.remote

import java.util

trait SvnkitRemoteService {

  /* Repository */
  /**
    * 创建本地仓库
    *
    * @param server 服务器
    * @param repositoryName
    * @param enableRevisionProperties
    * @param force
    * @throws
    */
  @throws[Exception]
  def createLocalRepository(server: String, repositoryName: String, enableRevisionProperties: Boolean, force: Boolean): Unit

  /**
    * 创建本地仓库并授权
    *
    * @param server 服务器
    * @param repositoryName
    * @param enableRevisionProperties
    * @param force
    * @param values
    * @throws
    */
  @throws[Exception]
  def createLocalRepository(server: String, repositoryName: String, enableRevisionProperties: Boolean, force: Boolean, values: util.Map[String, String]): Unit

  /**
    * 批量创建本地仓库
    *
    * @param server 服务器
    * @param repositoryNames
    * @param enableRevisionProperties
    * @param force
    * @throws
    */
  @throws[Exception]
  def createLocalRepositoryBatch(server: String, repositoryNames: util.List[String], enableRevisionProperties: Boolean, force: Boolean): Unit

  /**
    * 批量创建本地仓库并授权
    *
    * @param server 服务器
    * @param repositoryNames
    * @param enableRevisionProperties
    * @param force
    * @param values
    * @throws
    */
  @throws[Exception]
  def createLocalRepositoryBatch(server: String, repositoryNames: util.List[String], enableRevisionProperties: Boolean, force: Boolean, values: util.Map[String, String]): Unit

  /**
    * 列出条目权限
    *
    * @param server         服务器
    * @param repositoryName 仓库名称
    * @param repositoryPath 条目路径
    * @return
    */
  @throws[Exception]
  def listRights(server: String, repositoryName: String, repositoryPath: String): util.Map[String, util.Map[String, String]]

  /**
    * 列出当前仓库所有用户
    *
    * @param server         服务器
    * @param repositoryName 仓库名称
    * @return Map(对象 -> 权限)
    */
  @throws[Exception]
  def listRightUsers(server: String, repositoryName: String): util.Map[String, String]

  /**
    * 仓库条目授权
    *
    * @param server         服务器
    * @param repositoryName 仓库名称
    * @param repositoryPath 条目路径
    * @param values         权限集
    */
  @throws[Exception]
  def addRights(server: String, repositoryName: String, repositoryPath: String, values: util.Map[String, String]): Unit

  /**
    * 删除对象权限
    *
    * @param server         服务器
    * @param repositoryName 仓库名称
    * @param repositoryPath 条目路径
    * @param names          对象名称
    * @return
    */
  @throws[Exception]
  def deleteRights(server: String, repositoryName: String, repositoryPath: String, names: Array[String]): Unit

  /**
    * 列出所有仓库
    *
    * @param server 服务器
    * @return
    */
  @throws[Exception]
  def listRepositories(server: String): util.List[String]

  /**
    * 列出用户有权限的仓库
    *
    * @param server   服务器
    * @param username 用户名称
    * @return
    */
  @throws[Exception]
  def userHaveRightRepositories(server: String, username: String): util.List[String]

  /**
    * 列出组别有权限的仓库
    *
    * @param server    服务器
    * @param groupName 组别名称
    * @return
    */
  def groupHaveRightRepositories(server: String, groupName: String): util.List[String]


  /* Group */
  /**
    * 加载所有组信息
    *
    * @param server 服务器
    * @return
    */
  @throws[Exception]
  def loadAllGroups(server: String): util.Map[String, Array[String]]

  /**
    * 列出所有组别
    *
    * @param server 服务器
    * @return
    */
  @throws[Exception]
  def listGroups(server: String): util.List[String]

  /**
    * 创建组别
    *
    * @param server 服务器
    * @param name   名称
    */
  @throws[Exception]
  def createGroup(server: String, name: String): Unit

  /**
    * 批量创建组别
    *
    * @param server 服务器
    * @param names  名称
    */
  @throws[Exception]
  def createGroupBatch(server: String, names: util.List[String]): Unit

  /**
    * 设置组别用户
    *
    * @param server 服务器
    * @param name   名称
    * @param users  用户
    */
  @throws[Exception]
  def setGroupUsers(server: String, name: String, users: String): Unit


  /* User */
  /**
    * 列出所有用户
    *
    * @param server 服务器
    * @return
    */
  @throws[Exception]
  def listUsers(server: String): util.List[String]

  /**
    * 创建用户
    *
    * @param server   服务器
    * @param username 用户名称
    * @param password 用户密码
    */
  @throws[Exception]
  def createUser(server: String, username: String, password: String): Unit

  /**
    * 批量创建用户
    *
    * @param server 服务器
    * @param users
    */
  @throws[Exception]
  def createUserBatch(server: String, users: util.Map[String, String]): Unit

  /**
    * 修改用户
    *
    * @param server   服务器
    * @param username 用户名称
    * @param password 用户密码
    */
  @throws[Exception]
  def updateUser(server: String, username: String, password: String): Unit
}
