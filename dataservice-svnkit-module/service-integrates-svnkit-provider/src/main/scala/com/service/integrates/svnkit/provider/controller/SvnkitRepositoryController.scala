package com.service.integrates.svnkit.provider.controller

import java.util

import com.service.framework.core.serializer.ServiceSerializer
import com.service.integrates.svnkit.api.service.SvnkitRepositoryOperationService
import io.swagger.annotations.{Api, ApiOperation}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{PostMapping, RequestMapping, RestController}

import scala.collection.JavaConversions._

@RestController
@RequestMapping(value = Array("/svnkit/provider/repositories"))
@Api("SVN仓库操作管理")
class SvnkitRepositoryController {

  /* 本地服务 */

  @Autowired
  val repositoryService: SvnkitRepositoryOperationService = null

  @Autowired
  val serviceSerializer: ServiceSerializer = null

  @throws[Exception]
  @ApiOperation("创建本地仓库")
  @PostMapping(value = Array("/createLocalRepository"))
  def createLocalRepository(repositoryName: String, enableRevisionProperties: Boolean, force: Boolean): Unit = {
    repositoryService.createLocalRepository(repositoryName, enableRevisionProperties, force)
  }

  @throws[Exception]
  @ApiOperation("创建本地仓库并授权")
  @PostMapping(value = Array("/createLocalRepositoryAuth"))
  def createLocalRepository(repositoryName: String, enableRevisionProperties: Boolean, force: Boolean, values: Array[Byte]): Unit = {
    repositoryService.createLocalRepository(repositoryName, enableRevisionProperties, force, serviceSerializer.deserialize(values, classOf[util.Map[String, String]]))
  }

  @throws[Exception]
  @ApiOperation("批量创建本地仓库")
  @PostMapping(value = Array("/createLocalRepositoryBatch"))
  def createLocalRepositoryBatch(repositoryNames: Array[String], enableRevisionProperties: Boolean, force: Boolean): Unit = {
    repositoryService.createLocalRepositoryBatch(repositoryNames.toList, enableRevisionProperties, force)
  }

  @throws[Exception]
  @ApiOperation("批量创建本地仓库并授权")
  @PostMapping(value = Array("/createLocalRepositoryBatchAuth"))
  def createLocalRepositoryBatch(repositoryNames: Array[String], enableRevisionProperties: Boolean, force: Boolean, values: Array[Byte]): Unit = {
    repositoryService.createLocalRepositoryBatch(repositoryNames.toList, enableRevisionProperties, force, serviceSerializer.deserialize(values, classOf[util.Map[String, String]]))
  }

  /**
    * 列出条目权限
    *
    * @param repositoryName 仓库名称
    * @param repositoryPath 条目路径
    * @return
    */
  @throws[Exception]
  @ApiOperation("列出条目权限")
  @PostMapping(value = Array("/listRights"))
  def listRights(repositoryName: String, repositoryPath: String): util.Map[String, util.Map[String, String]] = {
    repositoryService.listRights(repositoryName, repositoryPath)
  }

  /**
    * 列出当前仓库所有用户
    *
    * @param repositoryName 仓库名称
    * @return Map(对象 -> 权限)
    */
  @throws[Exception]
  @ApiOperation("列出当前仓库所有用户")
  @PostMapping(value = Array("/listRightUsers"))
  def listRightUsers(repositoryName: String): util.Map[String, String] = {
    repositoryService.listRightUsers(repositoryName)
  }

  /**
    * 仓库条目授权
    *
    * @param repositoryName 仓库名称
    * @param repositoryPath 条目路径
    * @param values         权限集
    */
  @throws[Exception]
  @ApiOperation("仓库条目授权")
  @PostMapping(value = Array("/addRights"))
  def addRights(repositoryName: String, repositoryPath: String, values: Array[Byte]): Unit = {
    repositoryService.addRights(repositoryName, repositoryPath, serviceSerializer.deserialize(values, classOf[util.Map[String, String]]))
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
  @ApiOperation("删除对象权限")
  @PostMapping(value = Array("/deleteRights"))
  def deleteRights(repositoryName: String, repositoryPath: String, names: Array[String]): Unit = {
    repositoryService.deleteRights(repositoryName, repositoryPath, names)
  }

  /**
    * 列出所有仓库
    *
    * @return
    */
  @throws[Exception]
  @ApiOperation("列出所有仓库")
  @PostMapping(value = Array("/listRepositories"))
  def listRepositories(): util.List[String] = {
    repositoryService.listRepositories()
  }

  /**
    * 列出有权限的仓库
    *
    * @param username 用户名称
    * @return
    */
  @throws[Exception]
  @ApiOperation("列出用戶有权限的仓库")
  @PostMapping(value = Array("/userHaveRightRepositories"))
  def userHaveRightRepositories(username: String): util.List[String] = {
    repositoryService.userHaveRightRepositories(username)
  }

  /**
    * 列出有权限的仓库
    *
    * @param groupName 组别名称
    * @return
    */
  @throws[Exception]
  @ApiOperation("列出组别有权限的仓库")
  @PostMapping(value = Array("/groupHaveRightRepositories"))
  def groupHaveRightRepositories(groupName: String): util.List[String] = {
    repositoryService.groupHaveRightRepositories(groupName)
  }
}
