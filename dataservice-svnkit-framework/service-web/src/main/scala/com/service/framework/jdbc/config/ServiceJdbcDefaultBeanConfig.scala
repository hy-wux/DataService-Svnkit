package com.service.framework.jdbc.config

import com.service.framework.jdbc.entity.{DictCode, Params}
import com.service.framework.jdbc.service.{ServiceDictCodeService, ServiceParamsService}
import com.service.framework.jdbc.service.impl.{ServiceDictCodeServiceImpl, ServiceParamsServiceImpl}
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.{Bean, Configuration}

@Configuration
class ServiceJdbcDefaultBeanConfig {
  /**
    * 默认的数据字典实现
    *
    * @return
    */
  @ConditionalOnMissingBean(name = Array("dictCode"))
  @Bean(name = Array("dictCode"))
  def baseDictCodeService: ServiceDictCodeService[DictCode] = new ServiceDictCodeServiceImpl

  /**
    * 默认的数据字典实现
    *
    * @return
    */
  @ConditionalOnMissingBean(name = Array("parameters"))
  @Bean(name = Array("parameters"))
  def baseParamsService: ServiceParamsService[Params] = new ServiceParamsServiceImpl
}
