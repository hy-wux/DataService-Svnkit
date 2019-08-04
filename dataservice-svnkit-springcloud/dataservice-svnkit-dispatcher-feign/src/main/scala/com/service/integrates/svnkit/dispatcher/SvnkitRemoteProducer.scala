package com.service.integrates.svnkit.dispatcher

import java.util

import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.{PostMapping, RequestParam}

import scala.collection.JavaConversions._

trait SvnkitRemoteProducer {
  /* 仓库 */
  @throws[Exception]
  @PostMapping(value = Array("/svnkit/provider/repositories/createLocalRepository"))
  def createLocalRepository(@RequestParam(value = "repositoryName") repositoryName: String,
                            @RequestParam(value = "enableRevisionProperties") enableRevisionProperties: Boolean,
                            @RequestParam(value = "force") force: Boolean): Unit

  @throws[Exception]
  @PostMapping(value = Array("/svnkit/provider/repositories/createLocalRepositoryAuth"))
  def createLocalRepository(@RequestParam(value = "repositoryName") repositoryName: String,
                            @RequestParam(value = "enableRevisionProperties") enableRevisionProperties: Boolean,
                            @RequestParam(value = "force") force: Boolean,
                            @RequestParam(value = "values") values: Array[Byte]): Unit

  @throws[Exception]
  @PostMapping(value = Array("/svnkit/provider/repositories/createLocalRepositoryBatch"))
  def createLocalRepositoryBatch(@RequestParam(value = "repositoryNames") repositoryNames: Array[String],
                                 @RequestParam(value = "enableRevisionProperties") enableRevisionProperties: Boolean,
                                 @RequestParam(value = "force") force: Boolean): Unit

  @throws[Exception]
  @PostMapping(value = Array("/svnkit/provider/repositories/createLocalRepositoryBatchAuth"))
  def createLocalRepositoryBatch(@RequestParam(value = "repositoryNames") repositoryNames: Array[String],
                                 @RequestParam(value = "enableRevisionProperties") enableRevisionProperties: Boolean,
                                 @RequestParam(value = "force") force: Boolean,
                                 @RequestParam(value = "values") values: Array[Byte]): Unit

  /**
    * 列出条目权限
    *
    * @param repositoryName 仓库名称
    * @param repositoryPath 条目路径
    * @return
    */
  @throws[Exception]
  @PostMapping(value = Array("/svnkit/provider/repositories/listRights"))
  def listRights(@RequestParam(value = "repositoryName") repositoryName: String,
                 @RequestParam(value = "repositoryPath") repositoryPath: String): util.Map[String, util.Map[String, String]]

  /**
    * 列出当前仓库所有用户
    *
    * @param repositoryName 仓库名称
    * @return Map(对象 -> 权限)
    */
  @throws[Exception]
  @PostMapping(value = Array("/svnkit/provider/repositories/listRightUsers"))
  def listRightUsers(@RequestParam(value = "repositoryName") repositoryName: String): util.Map[String, String]

  /**
    * 仓库条目授权
    *
    * @param repositoryName 仓库名称
    * @param repositoryPath 条目路径
    * @param values         权限集
    */
  @throws[Exception]
  @PostMapping(value = Array("/svnkit/provider/repositories/addRights"))
  def addRights(@RequestParam(value = "repositoryName") repositoryName: String,
                @RequestParam(value = "repositoryPath") repositoryPath: String,
                @RequestParam(value = "values") values: Array[Byte]): Unit

  /**
    * 删除对象权限
    *
    * @param repositoryName 仓库名称
    * @param repositoryPath 条目路径
    * @param names          对象名称
    * @return
    */
  @throws[Exception]
  @PostMapping(value = Array("/svnkit/provider/repositories/deleteRights"))
  def deleteRights(@RequestParam(value = "repositoryName") repositoryName: String,
                   @RequestParam(value = "repositoryPath") repositoryPath: String,
                   @RequestParam(value = "names") names: Array[String]): Unit

  /**
    * 列出所有仓库
    *
    * @return
    */
  @throws[Exception]
  @PostMapping(value = Array("/svnkit/provider/repositories/listRepositories"))
  def listRepositories(): util.List[String]

  /**
    * 列出用户有权限的仓库
    *
    * @param username 用户名称
    * @return
    */
  @throws[Exception]
  @PostMapping(value = Array("/svnkit/provider/repositories/userHaveRightRepositories"))
  def userHaveRightRepositories(@RequestParam(value = "username") username: String): util.List[String]

  /**
    * 列出组别有权限的仓库
    *
    * @param groupName 组别名称
    * @return
    */
  @throws[Exception]
  @PostMapping(value = Array("/svnkit/provider/repositories/groupHaveRightRepositories"))
  def groupHaveRightRepositories(@RequestParam(value = "groupName") groupName: String): util.List[String]

  /* 组别 */

  /**
    * 加载所有组信息
    *
    * @return
    */
  @throws[Exception]
  @PostMapping(value = Array("/svnkit/provider/groups/loadAllGroups"))
  def loadAllGroups(): util.Map[String, Array[String]]

  /**
    * 列出所有组别
    *
    * @return
    */
  @throws[Exception]
  @PostMapping(value = Array("/svnkit/provider/groups/listGroups"))
  def listGroups(): util.List[String]

  /**
    * 创建组别
    *
    * @param groupName 名称
    */
  @throws[Exception]
  @PostMapping(value = Array("/svnkit/provider/groups/createGroup"))
  def createGroup(@RequestParam(value = "groupName") groupName: String): Unit

  /**
    * 批量创建组别
    *
    * @param groupNames 名称
    */
  @throws[Exception]
  @PostMapping(value = Array("/svnkit/provider/groups/createGroupBatch"))
  def createGroupBatch(@RequestParam(value = "groupNames") groupNames: Array[String]): Unit

  /**
    * 设置组别用户
    *
    * @param groupName 名称
    * @param users     用户
    */
  @throws[Exception]
  @PostMapping(value = Array("/svnkit/provider/groups/setGroupUsers"))
  def setGroupUsers(@RequestParam(value = "groupName") groupName: String,
                    @RequestParam(value = "users") users: String): Unit

  /* 用户 */

  /**
    * 列出所有用户
    *
    * @return
    */
  @throws[Exception]
  @PostMapping(value = Array("/svnkit/provider/users/listUsers"))
  def listUsers(): util.List[String]

  /**
    * 创建用户
    *
    * @param username 用户名称
    * @param password 用户密码
    */
  @throws[Exception]
  @PostMapping(value = Array("/svnkit/provider/users/createUser"))
  def createUser(@RequestParam(value = "username") username: String,
                 @RequestParam(value = "password") password: String): Unit

  /**
    * 批量创建用户
    *
    * @param users
    */
  @throws[Exception]
  @PostMapping(value = Array("/svnkit/provider/users/createUserBatch"))
  def createUserBatch(@RequestParam(value = "users") users: Array[Byte]): Unit

  /**
    * 修改用户
    *
    * @param username 用户名称
    * @param password 用户密码
    */
  @throws[Exception]
  @PostMapping(value = Array("/svnkit/provider/users/updateUser"))
  def updateUser(@RequestParam(value = "username") username: String,
                 @RequestParam(value = "password") password: String): Unit
}

@Component
class SvnkitRemoteProducerFallback extends SvnkitRemoteProducer {
  /* 仓库 */
  @throws[Exception]
  def createLocalRepository(@RequestParam(value = "repositoryName") repositoryName: String,
                            @RequestParam(value = "enableRevisionProperties") enableRevisionProperties: Boolean,
                            @RequestParam(value = "force") force: Boolean): Unit = {
  }

  @throws[Exception]
  def createLocalRepository(@RequestParam(value = "repositoryName") repositoryName: String,
                            @RequestParam(value = "enableRevisionProperties") enableRevisionProperties: Boolean,
                            @RequestParam(value = "force") force: Boolean,
                            @RequestParam(value = "values") values: Array[Byte]): Unit = {
  }

  @throws[Exception]
  def createLocalRepositoryBatch(@RequestParam(value = "repositoryNames") repositoryNames: Array[String],
                                 @RequestParam(value = "enableRevisionProperties") enableRevisionProperties: Boolean,
                                 @RequestParam(value = "force") force: Boolean): Unit = {
  }

  @throws[Exception]
  def createLocalRepositoryBatch(@RequestParam(value = "repositoryNames") repositoryNames: Array[String],
                                 @RequestParam(value = "enableRevisionProperties") enableRevisionProperties: Boolean,
                                 @RequestParam(value = "force") force: Boolean,
                                 @RequestParam(value = "values") values: Array[Byte]): Unit = {
  }

  /**
    * 列出条目权限
    *
    * @param repositoryName 仓库名称
    * @param repositoryPath 条目路径
    * @return
    */
  @throws[Exception]
  def listRights(@RequestParam(value = "repositoryName") repositoryName: String,
                 @RequestParam(value = "repositoryPath") repositoryPath: String): util.Map[String, util.Map[String, String]] = {
    Map[String, util.Map[String, String]]()
  }

  /**
    * 列出当前仓库所有用户
    *
    * @param repositoryName 仓库名称
    * @return Map(对象 -> 权限)
    */
  @throws[Exception]
  def listRightUsers(@RequestParam(value = "repositoryName") repositoryName: String): util.Map[String, String] = {
    Map[String, String]()
  }

  /**
    * 仓库条目授权
    *
    * @param repositoryName 仓库名称
    * @param repositoryPath 条目路径
    * @param values         权限集
    */
  @throws[Exception]
  def addRights(@RequestParam(value = "repositoryName") repositoryName: String,
                @RequestParam(value = "repositoryPath") repositoryPath: String,
                @RequestParam(value = "values") values: Array[Byte]): Unit = {
  }

  /**
    * 删除对象权限
    *
    * @param repositoryName 仓库名称
    * @param repositoryPath 条目路径
    * @param names          对象名称
    * @return
    */
  @throws[Exception]
  def deleteRights(@RequestParam(value = "repositoryName") repositoryName: String,
                   @RequestParam(value = "repositoryPath") repositoryPath: String,
                   @RequestParam(value = "names") names: Array[String]): Unit = {
  }

  /**
    * 列出所有仓库
    *
    * @return
    */
  @throws[Exception]
  def listRepositories(): util.List[String] = {
    List[String]()
  }

  /**
    * 列出用户有权限的仓库
    *
    * @param username 用户名称
    * @return
    */
  @throws[Exception]
  def userHaveRightRepositories(@RequestParam(value = "username") username: String): util.List[String] = {
    List[String]()
  }

  /**
    * 列出组别有权限的仓库
    *
    * @param groupName 组别名称
    * @return
    */
  @throws[Exception]
  def groupHaveRightRepositories(@RequestParam(value = "groupName") groupName: String): util.List[String] = {
    List[String]()
  }

  /* 组别 */

  /**
    * 加载所有组信息
    *
    * @return
    */
  @throws[Exception]
  def loadAllGroups(): util.Map[String, Array[String]] = {
    Map[String, Array[String]]()
  }

  /**
    * 列出所有组别
    *
    * @return
    */
  @throws[Exception]
  def listGroups(): util.List[String] = {
    List[String]()
  }

  /**
    * 创建组别
    *
    * @param groupName 名称
    */
  @throws[Exception]
  def createGroup(@RequestParam(value = "groupName") groupName: String): Unit = {
  }

  /**
    * 批量创建组别
    *
    * @param groupNames 名称
    */
  @throws[Exception]
  def createGroupBatch(@RequestParam(value = "groupNames") groupNames: Array[String]): Unit = {
  }

  /**
    * 设置组别用户
    *
    * @param groupName 名称
    * @param users     用户
    */
  @throws[Exception]
  def setGroupUsers(@RequestParam(value = "groupName") groupName: String,
                    @RequestParam(value = "users") users: String): Unit = {
  }

  /* 用户 */

  /**
    * 列出所有用户
    *
    * @return
    */
  @throws[Exception]
  def listUsers(): util.List[String] = {
    List[String]()
  }

  /**
    * 创建用户
    *
    * @param username 用户名称
    * @param password 用户密码
    */
  @throws[Exception]
  def createUser(@RequestParam(value = "username") username: String,
                 @RequestParam(value = "password") password: String): Unit = {
  }

  /**
    * 批量创建用户
    *
    * @param users
    */
  @throws[Exception]
  def createUserBatch(@RequestParam(value = "users") users: Array[Byte]): Unit = {
  }

  /**
    * 修改用户
    *
    * @param username 用户名称
    * @param password 用户密码
    */
  @throws[Exception]
  def updateUser(@RequestParam(value = "username") username: String,
                 @RequestParam(value = "password") password: String): Unit = {
  }
}