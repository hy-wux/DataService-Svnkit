package com.service.integrates.svnkit.api.service

import java.util

import com.service.integrates.svnkit.api.ServiceSvnkitConfigurationProperties
import org.springframework.beans.factory.annotation.Autowired

trait SvnkitUserOperationService {

  @Autowired
  val property: ServiceSvnkitConfigurationProperties = null

  /**
    * 列出所有用户
    *
    * @return
    */
  @throws[Exception]
  def listUsers(): util.List[String]

  /**
    * 创建用户
    *
    * @param username 用户名称
    * @param password 用户密码
    */
  @throws[Exception]
  def createUser(username: String, password: String): Unit

  /**
    * 批量创建用户
    *
    * @param users
    */
  @throws[Exception]
  def createUserBatch(users: util.Map[String, String]): Unit

  /**
    * 修改用户
    *
    * @param username 用户名称
    * @param password 用户密码
    */
  @throws[Exception]
  def updateUser(username: String, password: String): Unit
}
