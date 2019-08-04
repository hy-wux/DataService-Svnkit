package com.service.integrates.email.core

import java.io.File

import com.service.framework.core.utils.CommUtil
import javax.mail.internet.MimeMessage
import org.springframework.core.io.Resource
import org.springframework.mail.javamail.{JavaMailSender, MimeMessageHelper}
import org.springframework.scheduling.annotation.Async

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
  @Async(value = "ServiceExecutor")
  def sendEmail(toUsers: Array[String], ccUsers: Array[String], bccUsers: Array[String], subject: String, content: String): Unit = {
    val message: MimeMessage = mailSender.createMimeMessage()

    try {
      val helper: MimeMessageHelper = new MimeMessageHelper(message, true)
      if (CommUtil.isEmpty(personal)) {
        helper.setFrom(from)
      } else {
        helper.setFrom(from, personal)
      }
      // 收件人
      helper.setTo(toUsers)
      // 抄送人
      if (CommUtil.isNotEmpty(ccUsers)) helper.setCc(ccUsers)
      // 密送人
      if (CommUtil.isNotEmpty(bccUsers)) helper.setBcc(bccUsers)
      // 邮件主题
      helper.setSubject(subject)
      // 邮件内容
      helper.setText(content, true)

      mailSender.send(message)
    } catch {
      case ex: Exception => ex.printStackTrace()
    }
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
  @Async(value = "ServiceExecutor")
  def sendEmailByFile(toUsers: Array[String], ccUsers: Array[String], bccUsers: Array[String], subject: String, attachments: Map[String, File], inlines: Map[String, File], content: String): Unit = {
    val message: MimeMessage = mailSender.createMimeMessage()

    try {
      val helper: MimeMessageHelper = new MimeMessageHelper(message, true)
      if (CommUtil.isEmpty(personal)) {
        helper.setFrom(from)
      } else {
        helper.setFrom(from, personal)
      }
      // 收件人
      helper.setTo(toUsers)
      // 抄送人
      if (CommUtil.isNotEmpty(ccUsers)) helper.setCc(ccUsers)
      // 密送人
      if (CommUtil.isNotEmpty(bccUsers)) helper.setBcc(bccUsers)
      // 邮件主题
      helper.setSubject(subject)
      // 邮件内容
      helper.setText(content, true)
      // 添加附件
      if (CommUtil.isNotEmpty(attachments)) {
        attachments.foreach(attachment => {
          helper.addAttachment(attachment._1, attachment._2)
        })
      }
      // 添加静态资源
      if (CommUtil.isNotEmpty(inlines)) {
        inlines.foreach(inline => {
          helper.addInline(inline._1, inline._2)
        })
      }
      mailSender.send(message)
    } catch {
      case ex: Exception => ex.printStackTrace()
    }
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
  @Async(value = "ServiceExecutor")
  def sendEmailByResource(toUsers: Array[String], ccUsers: Array[String], bccUsers: Array[String], subject: String, attachments: Map[String, Resource], inlines: Map[String, Resource], content: String): Unit = {
    val message: MimeMessage = mailSender.createMimeMessage()

    try {
      val helper: MimeMessageHelper = new MimeMessageHelper(message, true)
      if (CommUtil.isEmpty(personal)) {
        helper.setFrom(from)
      } else {
        helper.setFrom(from, personal)
      }
      // 收件人
      helper.setTo(toUsers)
      // 抄送人
      if (CommUtil.isNotEmpty(ccUsers)) helper.setCc(ccUsers)
      // 密送人
      if (CommUtil.isNotEmpty(bccUsers)) helper.setBcc(bccUsers)
      // 邮件主题
      helper.setSubject(subject)
      // 邮件内容
      helper.setText(content, true)
      // 添加附件
      if (CommUtil.isNotEmpty(attachments)) {
        attachments.foreach(attachment => {
          helper.addAttachment(attachment._1, attachment._2)
        })
      }
      // 添加静态资源
      if (CommUtil.isNotEmpty(inlines)) {
        inlines.foreach(inline => {
          helper.addInline(inline._1, inline._2)
        })
      }
      mailSender.send(message)
    } catch {
      case ex: Exception => ex.printStackTrace()
    }
  }
}
