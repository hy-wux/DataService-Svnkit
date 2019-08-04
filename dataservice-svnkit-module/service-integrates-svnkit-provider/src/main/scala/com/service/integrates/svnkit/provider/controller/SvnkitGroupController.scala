package com.service.integrates.svnkit.provider.controller

import java.util

import com.service.integrates.svnkit.api.service.SvnkitGroupOperationService
import io.swagger.annotations.{Api, ApiOperation}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{PostMapping, RequestMapping, RestController}

import scala.collection.JavaConversions._

@RestController
@RequestMapping(value = Array("/svnkit/provider/groups"))
@Api("SVN组别操作管理")
class SvnkitGroupController {

  /* 本地服务 */

  @Autowired
  val groupService: SvnkitGroupOperationService = null

  /**
    * 加载所有组信息
    *
    * @return
    */
  @throws[Exception]
  @ApiOperation("加载所有组信息")
  @PostMapping(value = Array("/loadAllGroups"))
  def loadAllGroups(): util.Map[String, Array[String]] = {
    groupService.loadAllGroups()
  }

  /**
    * 列出所有组别
    *
    * @return
    */
  @throws[Exception]
  @ApiOperation("列出所有组别")
  @PostMapping(value = Array("/listGroups"))
  def listGroups(): util.List[String] = {
    groupService.listGroups()
  }

  /**
    * 创建组别
    *
    * @param groupName 名称
    */
  @throws[Exception]
  @ApiOperation("创建组别")
  @PostMapping(value = Array("/createGroup"))
  def createGroup(groupName: String): Unit = {
    groupService.createGroup(groupName)
  }

  /**
    * 批量创建组别
    *
    * @param groupNames 名称
    */
  @throws[Exception]
  @ApiOperation("批量创建组别")
  @PostMapping(value = Array("/createGroupBatch"))
  def createGroupBatch(groupNames: Array[String]): Unit = {
    groupService.createGroupBatch(groupNames.toList)
  }

  /**
    * 设置组别用户
    *
    * @param groupName 名称
    * @param users     用户
    */
  @throws[Exception]
  @ApiOperation("设置组别用户")
  @PostMapping(value = Array("/setGroupUsers"))
  def setGroupUsers(groupName: String, users: String): Unit = {
    groupService.setGroupUsers(groupName, users)
  }
}
