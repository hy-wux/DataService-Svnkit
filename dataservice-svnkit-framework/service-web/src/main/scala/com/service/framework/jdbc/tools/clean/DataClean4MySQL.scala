package com.service.framework.jdbc.tools.clean

import com.service.framework.core.logs.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

/**
  * @author 伍鲜
  *
  *         数据清理MySQL实现
  */
@Component
@ConditionalOnProperty(value = Array("service.jdbc.database-type"), havingValue = "mysql")
class DataClean4MySQL extends DataClean with Logging {

  @Autowired
  val jdbcTemplate: JdbcTemplate = null

  /**
    * 清理数据
    *
    * @param date   批量日期
    * @param tables 需要清理的表
    */
  override def cleanData(date: String, tables: Array[(String, String, Int)]): Unit = {
    debug("MySQL数据库的数据清理")
    tables.par.foreach(table => {
      debug(s"清理${table._1}表的${table._3}天前的数据")
      jdbcTemplate.execute(s"delete from ${table._1} where DATEDIFF(STR_TO_DATE('${date}','%Y%m%d'), ${table._2}) > ${table._3}")
    })
  }
}
