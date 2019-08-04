package com.service.integrates.email.config

import com.service.integrates.email.component.ServiceEmailComponent
import com.service.integrates.email.core.ServiceEmailSender
import javax.annotation.PostConstruct
import org.springframework.beans.factory.annotation.{Autowired, Value}
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender

@Configuration
@ConditionalOnProperty(value = Array("service.integrates.email.with-default"), havingValue = "true")
class ServiceEmailConfiguration {
  @Autowired
  val mailSender: JavaMailSender = null

  @Value("${spring.mail.username}")
  private val from: String = null

  @Autowired
  val serviceEmailComponent: ServiceEmailComponent = null

  @PostConstruct
  def initDefaultSender(): Unit = {
    val sender = new ServiceEmailSender()
    sender.mailSender = mailSender
    sender.from = from
    serviceEmailComponent.serviceEmailSenderMap.put("default", sender)
  }
}
