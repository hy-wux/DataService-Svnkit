package com.service.framework.jdbc.config

import java.sql.Connection

import com.service.framework.jdbc.interceptor.{MyCatInterceptor, OracleInterceptor}
import javax.sql.DataSource
import org.springframework.beans.factory.annotation.{Autowired, Qualifier}
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.jdbc.core.JdbcTemplate

/**
  * @author 伍鲜
  */
@Configuration
class ServiceJdbcDataSourceConfig {
  @Autowired
  @Qualifier("dataSource")
  val dataSource: DataSource = null

  @Bean
  def primaryJdbcTemplate: JdbcTemplate = new JdbcTemplate(dataSource)

  /**
    * 获取数据库连接
    */
  @Bean(name = Array("connection"))
  def connection: Connection = dataSource.getConnection

  @Bean
  @ConditionalOnProperty(value = Array("service.jdbc.mycat.hint"), havingValue = "remove")
  def MyCatInterceptor = new MyCatInterceptor

  @Bean
  @ConditionalOnProperty(value = Array("service.jdbc.database-type"), havingValue = "oracle")
  def OracleInterceptor = new OracleInterceptor
}
