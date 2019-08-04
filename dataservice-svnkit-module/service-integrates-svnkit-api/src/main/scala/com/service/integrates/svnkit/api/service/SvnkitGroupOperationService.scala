package com.service.integrates.svnkit.api.service

import java.util

import com.service.integrates.svnkit.api.ServiceSvnkitConfigurationProperties
import org.springframework.beans.factory.annotation.Autowired

trait SvnkitGroupOperationService {

  @Autowired
  val property: ServiceSvnkitConfigurationProperties = null

  /**
    * 加载所有组信息
    *
    * @return
    */
  @throws[Exception]
  def loadAllGroups(): util.Map[String, Array[String]]

  /**
    * 列出所有组别
    *
    * @return
    */
  @throws[Exception]
  def listGroups(): util.List[String]

  /**
    * 创建组别
    *
    * @param name 名称
    */
  @throws[Exception]
  def createGroup(name: String): Unit

  /**
    * 批量创建组别
    *
    * @param names 名称
    */
  @throws[Exception]
  def createGroupBatch(names: util.List[String]): Unit

  /**
    * 设置组别用户
    *
    * @param name  名称
    * @param users 用户
    */
  @throws[Exception]
  def setGroupUsers(name: String, users: String): Unit
}
