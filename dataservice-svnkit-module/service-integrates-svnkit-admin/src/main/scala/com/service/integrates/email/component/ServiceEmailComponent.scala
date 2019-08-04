package com.service.integrates.email.component

import java.util.Properties
import java.util.concurrent.ConcurrentHashMap

import com.service.integrates.email.core.ServiceEmailSender
import com.service.integrates.email.entity.{EmailAccount, EmailServer}
import com.service.integrates.email.service.{IEmailAccountService, IEmailServerService}
import com.sun.istack.internal.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.stereotype.Component

@Component
class ServiceEmailComponent {

  /**
    * 用于存储多Sender
    */
  private[email] val serviceEmailSenderMap: java.util.Map[String, ServiceEmailSender] = new ConcurrentHashMap[String, ServiceEmailSender]()

  @Autowired private val emailServerService: IEmailServerService = null
  @Autowired private val emailAccountService: IEmailAccountService = null

  def getEmailSender(@NotNull id: String = "default"): ServiceEmailSender = {
    if (!serviceEmailSenderMap.containsKey(id)) {
      initEmailSender(id)
    }
    serviceEmailSenderMap.get(id)
  }

  private[email] def initEmailSender(id: String): Unit = {
    val emailAccount = emailAccountService.selectById(id)
    val emailServer = emailServerService.selectById(emailAccount.serverKey)

    initEmailSender(emailAccount, emailServer)
  }

  private[email] def initEmailSender(emailAccount: EmailAccount, emailServer: EmailServer): Unit = {
    val serviceEmailSender = new ServiceEmailSender()
    val javaEmailSender = new JavaMailSenderImpl()

    javaEmailSender.setHost(emailServer.emailHost)
    javaEmailSender.setProtocol(emailServer.emailProtocol)
    javaEmailSender.setPort(emailServer.emailPort)
    javaEmailSender.setUsername(emailAccount.username)
    javaEmailSender.setPassword(emailAccount.password)

    javaEmailSender.setDefaultEncoding("UTF-8")

    val properties = new Properties()
    properties.put("mail.smtp.starttls.enable", "true")
    properties.put("mail.smtp.socketFactory.fallback", "true")
    javaEmailSender.setJavaMailProperties(properties)

    serviceEmailSender.mailSender = javaEmailSender
    serviceEmailSender.from = emailAccount.username
    serviceEmailSender.personal = emailAccount.accountName

    serviceEmailSenderMap.put(emailAccount.rowKey, serviceEmailSender)
  }

  private[email] def removeEmailSender(ids: List[String]): Unit = {
    ids.foreach(id => {
      serviceEmailSenderMap.remove(id)
    })
  }
}
