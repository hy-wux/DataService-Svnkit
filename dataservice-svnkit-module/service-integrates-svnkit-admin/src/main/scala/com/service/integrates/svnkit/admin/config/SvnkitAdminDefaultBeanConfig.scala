package com.service.integrates.svnkit.admin.config

import com.service.integrates.svnkit.admin.service.OnlineUserSession
import com.service.integrates.svnkit.admin.service.impl.OnlineUserSessionImpl
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.{Bean, Configuration}

@Configuration
class SvnkitAdminDefaultBeanConfig {
  /**
    * 默认的在线用户实现
    *
    * @return
    */
  @ConditionalOnMissingBean(Array(classOf[OnlineUserSession]))
  @Bean
  def baseOnlineUserSession: OnlineUserSession = new OnlineUserSessionImpl
}
