package com.service.framework.jdbc.service.impl

import com.service.framework.jdbc.entity.Params
import com.service.framework.jdbc.service.ServiceParamsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate

import scala.collection.JavaConversions._

/**
  * 数据字典的默认实现
  */
class ServiceParamsServiceImpl extends ServiceParamsService[Params] {
  @Autowired
  private val jdbcTemplate: JdbcTemplate = null

  /**
    * 根据key值获取value
    *
    * @param paramCode
    * @return
    */
  override def getParamValue(paramCode: String): Params = {
    jdbcTemplate.queryForList("select param_code, param_value from sys_params where param_code = ? and param_status = '01'", paramCode)
      .map(result => {
        val params = new Params
        params.paramCode = result.get("param_code").toString
        params.paramValue = result.get("param_value").toString
        params
      })
      .headOption
      .getOrElse(new Params)
  }
}
