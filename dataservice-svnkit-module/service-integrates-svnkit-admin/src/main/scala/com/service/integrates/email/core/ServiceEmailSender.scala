package com.service.integrates.email.core

import java.io.File

import org.springframework.core.io.Resource
import org.springframework.mail.javamail.JavaMailSender

import scala.beans.BeanProperty

class ServiceEmailSender private[email]() {
  @BeanProperty var mailSender: JavaMailSender = _
  @BeanProperty var from: String = _
  @BeanProperty var personal: String = _

  /**
    * 发送邮件
    *
    * @param toUsers  收件人
    * @param ccUsers  抄送人
    * @param bccUsers 密送人
    * @param subject  邮件主题
    * @param content  邮件内容
    */
  def sendEmail(toUsers: Array[String], ccUsers: Array[String], bccUsers: Array[String], subject: String, content: String): Unit = {
    val emailContent = new EmailContent
    emailContent.mailSender = mailSender
    emailContent.from = from
    emailContent.personal = personal
    emailContent.toUsers = toUsers
    emailContent.ccUsers = ccUsers
    emailContent.bccUsers = bccUsers
    emailContent.subject = subject
    emailContent.content = content
    emailContent.method = "sendEmail"
    emailContent.times = 1
    EmailQueue.blockingQueue.put(emailContent)
  }

  /**
    * 发送邮件
    *
    * @param toUsers     收件人
    * @param ccUsers     抄送人
    * @param bccUsers    密送人
    * @param subject     邮件主题
    * @param attachments 邮件附件
    * @param inlines     静态资源
    * @param content     邮件内容
    */
  def sendEmailByFile(toUsers: Array[String], ccUsers: Array[String], bccUsers: Array[String], subject: String, attachments: Map[String, File], inlines: Map[String, File], content: String): Unit = {
    val emailContent = new EmailContent
    emailContent.mailSender = mailSender
    emailContent.from = from
    emailContent.personal = personal
    emailContent.toUsers = toUsers
    emailContent.ccUsers = ccUsers
    emailContent.bccUsers = bccUsers
    emailContent.subject = subject
    emailContent.content = content
    emailContent.attachmentsFile = attachments
    emailContent.inlinesFile = inlines
    emailContent.method = "sendEmailByFile"
    emailContent.times = 1
    EmailQueue.blockingQueue.put(emailContent)
  }

  /**
    * 发送邮件
    *
    * @param toUsers     收件人
    * @param ccUsers     抄送人
    * @param bccUsers    密送人
    * @param subject     邮件主题
    * @param attachments 邮件附件
    * @param inlines     静态资源
    * @param content     邮件内容
    */
  def sendEmailByResource(toUsers: Array[String], ccUsers: Array[String], bccUsers: Array[String], subject: String, attachments: Map[String, Resource], inlines: Map[String, Resource], content: String): Unit = {
    val emailContent = new EmailContent
    emailContent.mailSender = mailSender
    emailContent.from = from
    emailContent.personal = personal
    emailContent.toUsers = toUsers
    emailContent.ccUsers = ccUsers
    emailContent.bccUsers = bccUsers
    emailContent.subject = subject
    emailContent.content = content
    emailContent.attachmentsResource = attachments
    emailContent.inlinesResource = inlines
    emailContent.method = "sendEmailByResource"
    emailContent.times = 1
    EmailQueue.blockingQueue.put(emailContent)
  }
}
