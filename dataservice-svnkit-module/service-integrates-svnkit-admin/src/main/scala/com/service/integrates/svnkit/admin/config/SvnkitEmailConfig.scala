package com.service.integrates.svnkit.admin.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.core.io.{ClassPathResource, Resource}

@Configuration
class SvnkitEmailConfig {

  @Bean(name = Array("svnkitEmailInlines"))
  @ConditionalOnMissingBean(name = Array("svnkitEmailInlines"))
  def svnkitEmailInlines(): Map[String, Resource] = {
    Map(
      "imageInEmailHead" -> new ClassPathResource("email/svnkit/imageInEmailHead.png"),
      "imageInEmailImgBlue" -> new ClassPathResource("email/svnkit/imageInEmailImgBlue.png"),
      "imageInEmailFooter" -> new ClassPathResource("email/svnkit/imageInEmailFooter.png")
    )
  }
}
