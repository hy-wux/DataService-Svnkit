package com.service.framework.jdbc.service.impl

import java.util

import com.service.framework.jdbc.entity.DictCode
import com.service.framework.jdbc.service.ServiceDictCodeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate

import scala.collection.JavaConversions._

/**
  * 数据字典的默认实现
  */
class ServiceDictCodeServiceImpl extends ServiceDictCodeService[DictCode] {
  @Autowired
  private val jdbcTemplate: JdbcTemplate = null

  /**
    * 根据字典类型获取字典列表
    *
    * @param dictType
    * @return
    */
  override def selectDictCodeByType(dictType: String): util.List[DictCode] = {
    val results = jdbcTemplate.queryForList("select code_value, code_text from sys_dict_code where dict_type = ? and code_status = '01'", dictType)
    results.map(result => {
      val dict = new DictCode
      dict.codeValue = result.get("code_value").toString
      dict.codeText = result.get("code_text").toString
      dict.asInstanceOf[DictCode]
    })
  }
}
