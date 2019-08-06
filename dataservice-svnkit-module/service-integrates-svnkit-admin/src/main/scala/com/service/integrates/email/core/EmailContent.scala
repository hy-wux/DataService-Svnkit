package com.service.integrates.email.core

import java.io.File

import org.springframework.core.io.Resource
import org.springframework.mail.javamail.JavaMailSender

import scala.beans.BeanProperty

/**
  * @author 伍鲜
  *
  *         封装发送邮件详情
  */
class EmailContent {
  /**
    * 调用方法
    */
  @BeanProperty var method: String = _
  /**
    * 发送次数，用来做失败重试的标志
    */
  @BeanProperty var times: Int = _
  /**
    * JavaMailSender，包含发件人账号、密码、发件服务器相关信息
    */
  @BeanProperty var mailSender: JavaMailSender = _
  /**
    * 发件人
    */
  @BeanProperty var from: String = _
  @BeanProperty var personal: String = _

  /**
    * 收件人
    */
  @BeanProperty var toUsers: Array[String] = _
  /**
    * 抄送
    */
  @BeanProperty var ccUsers: Array[String] = _
  /**
    * 密送
    */
  @BeanProperty var bccUsers: Array[String] = _
  /**
    * 主题
    */
  @BeanProperty var subject: String = _
  /**
    * 附件
    */
  @BeanProperty var attachmentsFile: Map[String, File] = _
  /**
    * 附件
    */
  @BeanProperty var attachmentsResource: Map[String, Resource] = _
  /**
    * 内联文件
    */
  @BeanProperty var inlinesFile: Map[String, File] = _
  /**
    * 内联文件
    */
  @BeanProperty var inlinesResource: Map[String, Resource] = _
  /**
    * 邮件内容
    */
  @BeanProperty var content: String = _
}
