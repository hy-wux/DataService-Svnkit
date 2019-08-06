package com.service.integrates.email.core

import java.io.File

import com.service.framework.core.logs.Logging
import com.service.framework.core.utils.CommUtil
import javax.annotation.PostConstruct
import javax.mail.internet.MimeMessage
import org.springframework.core.io.Resource
import org.springframework.mail.javamail.{JavaMailSender, MimeMessageHelper}
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

/**
  * @author 伍鲜
  *
  *         封装邮件发送方法
  */
@Component
class EmailSender extends Logging {

  /**
    * 监听邮件队列，发送邮件
    */
  @PostConstruct
  def startSend(): Unit = {
    new Thread() {
      override def run(): Unit = {
        while (true) {
          val emailContent = EmailQueue.blockingQueue.take()
          try {
            emailContent.method match {
              case "sendEmail" => {
                sendEmail(emailContent.mailSender, emailContent.from, emailContent.personal, emailContent.toUsers, emailContent.ccUsers, emailContent.bccUsers, emailContent.subject, emailContent.content)
              }
              case "sendEmailByFile" => {
                sendEmailByFile(emailContent.mailSender, emailContent.from, emailContent.personal, emailContent.toUsers, emailContent.ccUsers, emailContent.bccUsers, emailContent.subject, emailContent.attachmentsFile, emailContent.inlinesFile, emailContent.content)
              }
              case "sendEmailByResource" => {
                sendEmailByResource(emailContent.mailSender, emailContent.from, emailContent.personal, emailContent.toUsers, emailContent.ccUsers, emailContent.bccUsers, emailContent.subject, emailContent.attachmentsResource, emailContent.inlinesResource, emailContent.content)
              }
              case _ =>
            }
          } catch {
            case e: Throwable => {
              // 总共尝试发送5次
              if (emailContent.times < 5) {
                emailContent.times += 1
                EmailQueue.blockingQueue.put(emailContent)
              } else {
                error(e.getMessage)
              }
            }
          }
        }
      }
    }.start()
  }

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
  private def sendEmail(mailSender: JavaMailSender, from: String, personal: String, toUsers: Array[String], ccUsers: Array[String], bccUsers: Array[String], subject: String, content: String): Unit = {
    val message: MimeMessage = mailSender.createMimeMessage()

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
  private def sendEmailByFile(mailSender: JavaMailSender, from: String, personal: String, toUsers: Array[String], ccUsers: Array[String], bccUsers: Array[String], subject: String, attachments: Map[String, File], inlines: Map[String, File], content: String): Unit = {
    val message: MimeMessage = mailSender.createMimeMessage()

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
  private def sendEmailByResource(mailSender: JavaMailSender, from: String, personal: String, toUsers: Array[String], ccUsers: Array[String], bccUsers: Array[String], subject: String, attachments: Map[String, Resource], inlines: Map[String, Resource], content: String): Unit = {
    val message: MimeMessage = mailSender.createMimeMessage()

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
  }
}
