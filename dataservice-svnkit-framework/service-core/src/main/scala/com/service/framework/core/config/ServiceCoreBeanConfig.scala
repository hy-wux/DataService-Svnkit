package com.service.framework.core.config

import com.service.framework.core.serializer.{JavaSerializer, ServiceSerializer}
import org.springframework.context.annotation.{Bean, Configuration}

@Configuration
class ServiceCoreBeanConfig {
  @Bean
  def serviceSerializer(): ServiceSerializer = new JavaSerializer
}
