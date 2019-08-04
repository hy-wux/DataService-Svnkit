package com.service.framework.jdbc.tools.loader

import com.service.framework.core.PubFunction
import com.service.framework.core.component.IdWorker
import com.service.framework.core.logs.Logging
import com.service.framework.core.utils.CommUtil
import com.service.framework.jdbc.tools.loader.mapper.DataLoaderMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

import scala.collection.JavaConversions._

/**
  * @author 伍鲜
  *
  *         数据从文件加载到MySQL
  */
@Component
@ConditionalOnProperty(value = Array("service.jdbc.class-name.loader-text"), havingValue = "com.service.framework.data.tools.loader.Loader4TextMySQL")
class Loader4TextMySQL extends Loader4Text with PubFunction with Logging {

  @Autowired
  val configMapper: DataLoaderMapper = null

  @Autowired
  val idWorker: IdWorker = null

  @Autowired
  val jdbcTemplate: JdbcTemplate = null

  /**
    * 从文件加载数据到数据库。
    *
    * @param localFiles  数据文件绝对路径（多个文件用逗号分隔）
    * @param charset     字符集
    * @param withHead    是否包含标题行
    * @param splitString 字段分割
    * @param tableName   目标表名
    * @param loadMode    加载模式
    * @param options     其他选项
    * @return 加载成功返回true，否则返回false
    */
  override def dataLoader(localFiles: String, charset: String = "UTF-8", withHead: String = "no", splitString: String = "", tableName: String, loadMode: String = "", options: String): Boolean = {
    // 获取字段列表
    val columnsList = configMapper.queryLoaders(tableName)

    val mode = if (CommUtil.isNotEmpty(loadMode)) {
      loadMode.toLowerCase.trim match {
        case "truncate" => {
          jdbcTemplate.execute(s"truncate table ${tableName}")
          ""
        }
        case _ => {
          loadMode.toLowerCase.trim
        }
      }
    } else {
      ""
    }

    val sql =
      s"""load data local infile '${localFiles}' ${mode} into table ${tableName}
         ${if (CommUtil.isNotEmpty(charset)) s"CHARACTER SET '${charset.trim}'" else ""}
         FIELDS TERMINATED BY '${Hex2String(splitString)}'
         ${if ("yes".equalsIgnoreCase(withHead)) "IGNORE 1 LINES" else ""}
         ${
        if (columnsList.isEmpty) {
          ""
        } else {
          s"""${columnsList.map(column => s"@${column.columnName}").mkString("(", ",", ")")}
              set ${
            columnsList.map(column => {
              if (CommUtil.isEmpty(column.columnFormat))
                s"${column.columnName}=if(@${column.columnName}='',null,@${column.columnName})"
              else
                column.columnFormat
            }).mkString(",")
          }
           """
        }
      }
       """

    try {
      jdbcTemplate.execute(sql)
      true
    } catch {
      case ex: Exception => {
        error(ex.getMessage)
        false
      }
    }
  }
}