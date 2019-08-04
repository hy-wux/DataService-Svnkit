package com.service.integrates.svnkit.provider.controller

import java.util

import com.service.framework.core.serializer.ServiceSerializer
import com.service.integrates.svnkit.api.service.SvnkitUserOperationService
import io.swagger.annotations.{Api, ApiOperation}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{PostMapping, RequestMapping, RestController}

import scala.collection.JavaConversions._

@RestController
@RequestMapping(value = Array("/svnkit/provider/users"))
@Api("SVN用户操作管理")
class SvnkitUserController {

  /* 本地服务 */

  @Autowired
  val userService: SvnkitUserOperationService = null

  @Autowired
  val serviceSerializer: ServiceSerializer = null

  /**
    * 列出所有用户
    *
    * @return
    */
  @throws[Exception]
  @ApiOperation("列出所有用户")
  @PostMapping(value = Array("/listUsers"))
  def listUsers(): util.List[String] = {
    userService.listUsers()
  }

  /**
    * 创建用户
    *
    * @param username 用户名称
    * @param password 用户密码
    */
  @throws[Exception]
  @ApiOperation("创建用户")
  @PostMapping(value = Array("/createUser"))
  def createUser(username: String, password: String): Unit = {
    userService.createUser(username, password)
  }

  /**
    * 批量创建用户
    *
    * @param users
    */
  @throws[Exception]
  @ApiOperation("批量创建用户")
  @PostMapping(value = Array("/createUserBatch"))
  def createUserBatch(users: Array[Byte]): Unit = {
    userService.createUserBatch(serviceSerializer.deserialize(users, classOf[Map[String, String]]))
  }

  /**
    * 修改用户
    *
    * @param username 用户名称
    * @param password 用户密码
    */
  @throws[Exception]
  @ApiOperation("修改用户")
  @PostMapping(value = Array("/updateUser"))
  def updateUser(username: String, password: String): Unit = {
    userService.updateUser(username, password)
  }
}
