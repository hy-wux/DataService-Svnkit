package com.service.framework.jdbc.tools.export

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

import com.service.framework.core.utils.CommUtil
import com.service.framework.jdbc.config.properties.ServiceJdbcExportProperties
import com.service.framework.jdbc.tools.DBTools._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

/**
  * @author 伍鲜
  *
  *         从数据库导出数据到文件
  */
@Component
@ConditionalOnProperty(value = Array("service.jdbc.class-name.export-text"), havingValue = "com.service.framework.data.tools.export.Export2TextGeneric")
class Export2TextGeneric extends Export2Text {

  @Autowired
  val serviceJdbcExportProperty: ServiceJdbcExportProperties = null

  @Autowired
  val jdbcTemplate: JdbcTemplate = null

  /**
    * 按照指定字段分隔符，从数据库导出数据到文件。
    *
    * @param query       查询语句
    * @param filePath    数据文件的绝对路径
    * @param charset     字符集
    * @param withHead    是否包含标题行
    * @param splitString 字段分隔符
    * @return 导出成功返回true，否则返回false
    */
  override def export(query: String, filePath: String, charset: String, withHead: String, splitString: String): Boolean = {
    val timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    // 创建目标文件夹
    val fileDat = new File(filePath)
    fileDat.getAbsoluteFile().getParentFile().mkdirs()

    WriteToFile(fileDat, false, charset)(p => {
      val rs = jdbcTemplate.queryForRowSet(query)
      if (CommUtil.isNotEmpty(withHead)) {
        // 打印标题行
        if (withHead.equalsIgnoreCase("yes")) {
          p.write((1 to rs.getMetaData.getColumnCount).map(rs.getMetaData.getColumnName(_)).mkString(Hex2String(splitString)))
          p.newLine()
        }
      }

      var i = 0
      while (rs.next()) {
        if (i % 1000000 == 0) {
          println(f"${i}%12d rows exported at ${timestampFormat.format(new Date())}.")
        }
        p.write(rs.getLineData().mkString(Hex2String(splitString)))
        p.newLine()
        i += 1
      }
      println(f"${i}%12d rows exported at ${timestampFormat.format(new Date())}.")
      println(s"         output file ${filePath} closed at ${i} rows.")
    })

    if (serviceJdbcExportProperty.withChk.equalsIgnoreCase("true")) {
      createCheckFile(filePath, if (splitString == "") "20" else splitString)
    }

    true
  }

  /**
    * 按照指定字段长度，从数据库导出数据到文件。
    *
    * @param query    查询语句
    * @param filePath 数据文件的绝对路径
    * @param charset  字符集
    * @param withHead 是否包含标题行
    * @return 导出成功返回true，否则返回false
    */
  override def export(query: String, filePath: String, charset: String, withHead: String): Boolean = {
    export(query = query, filePath = filePath, charset = charset, withHead = withHead, splitString = "")
  }
}
