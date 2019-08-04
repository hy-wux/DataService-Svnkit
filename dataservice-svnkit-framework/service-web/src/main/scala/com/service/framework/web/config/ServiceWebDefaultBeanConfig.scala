package com.service.framework.web.config

import com.service.framework.web.service.ServicePermissionService
import com.service.framework.web.service.impl.ServicePermissionServiceImpl
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.{Bean, Configuration}

@Configuration
class ServiceWebDefaultBeanConfig {
  /**
    * 默认的用户权限实现
    *
    * @return
    */
  @ConditionalOnMissingBean(value = Array(classOf[ServicePermissionService]))
  @Bean(name = Array("permission"))
  def servicePermissionService: ServicePermissionService = new ServicePermissionServiceImpl
}
