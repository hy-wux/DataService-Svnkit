package com.service.framework.jdbc.tools.loader

import com.service.framework.core.PubFunction
import com.service.framework.core.component.IdWorker
import com.service.framework.core.logs.Logging
import com.service.framework.jdbc.tools.loader.mapper.DataLoaderMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

import scala.collection.JavaConversions._
import scala.io.Source

/**
  * @author 伍鲜
  *
  *         数据从文件加载到数据库
  */
@Component
@ConditionalOnProperty(value = Array("service.jdbc.class-name.loader-text"), havingValue = "com.service.framework.jdbc.tools.loader.Loader4TextGeneric")
class Loader4TextGeneric extends Loader4Text with PubFunction with Logging {

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
  override def dataLoader(localFiles: String, charset: String, withHead: String, splitString: String, tableName: String, loadMode: String, options: String): Boolean = {
    // 获取字段列表
    val columnsList = configMapper.queryLoaders(tableName)

    val lines = Source.fromFile(localFiles, charset).getLines().toList
    val data = ((if ("yes".equalsIgnoreCase(withHead)) 1 else 0) until lines.size).toList.map(index => {
      lines(index).split(Hex2String(splitString)).asInstanceOf[Array[AnyRef]]
    })

    jdbcTemplate.batchUpdate(s"insert into ${tableName}(${columnsList.map(_.columnName).mkString(",")}) values (${columnsList.map(_ => "?").mkString(",")})", data)

    true
  }
}