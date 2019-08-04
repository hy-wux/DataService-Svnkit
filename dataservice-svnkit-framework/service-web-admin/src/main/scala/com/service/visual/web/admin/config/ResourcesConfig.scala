package com.service.visual.web.admin.config

import com.service.framework.web.ServiceWebConfigurationProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.{Configuration, DependsOn}
import org.springframework.web.servlet.config.annotation.{ResourceHandlerRegistry, WebMvcConfigurer}

/**
  * @author 伍鲜
  *
  *         通用配置
  */
@Configuration
@DependsOn(Array("springContextHolder"))
class ResourcesConfig extends WebMvcConfigurer {
  @Autowired val properties: ServiceWebConfigurationProperties = null

  override def addResourceHandlers(registry: ResourceHandlerRegistry): Unit = {
    /** 头像上传路径 */
    registry.addResourceHandler("/profile/**").addResourceLocations("file:" + properties.getProfile)
  }
}
