package com.service.framework.jdbc.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

import scala.beans.BeanProperty

@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
class SpringDataSourceProperties {
  @BeanProperty var driverClassName: String = ""
  @BeanProperty var url: String = ""
  @BeanProperty var username: String = ""
  @BeanProperty var password: String = ""
}
