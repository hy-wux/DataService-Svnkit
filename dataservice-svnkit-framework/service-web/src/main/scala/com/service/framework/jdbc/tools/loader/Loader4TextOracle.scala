package com.service.framework.jdbc.tools.loader

import java.io.File

import com.service.framework.core.component.IdWorker
import com.service.framework.core.logs.Logging
import com.service.framework.core.utils.CommUtil
import com.service.framework.jdbc.tools.Function4Oracle
import com.service.framework.jdbc.tools.loader.mapper.DataLoaderMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

import scala.collection.JavaConversions._
import scala.io.Source
import scala.sys.process._

/**
  * @author 伍鲜
  *
  *         数据从文件加载到Oracle
  */
@Component
@ConditionalOnProperty(value = Array("service.jdbc.class-name.loader-text"), havingValue = "com.service.framework.data.tools.loader.Loader4TextOracle")
class Loader4TextOracle extends Loader4Text with Function4Oracle with Logging {

  val LoadMode: Map[String, String] = Map("append" -> "APPEND", "insert" -> "INSERT", "replace" -> "REPLACE", "truncate" -> "TRUNCATE")

  @Autowired
  val configMapper: DataLoaderMapper = null

  @Autowired
  val idWorker: IdWorker = null

  /**
    * 生成控制文件。
    *
    * @param ctlFile     控制文件绝对路径
    * @param datFiles    数据文件绝对路径（多个文件用逗号分隔）
    * @param charset     字符集
    * @param withHead    是否包含标题行
    * @param splitString 字段分隔符
    * @param tableName   目标表名称
    * @param loadMode    加载模式
    */
  private[this] def wirteCtl(ctlFile: String, datFiles: String, charset: String, withHead: String, splitString: String, tableName: String, loadMode: String): Unit = {
    WriteToFile(new File(ctlFile), false)(p => {
      p.println("load data")
      p.println("characterset %s" format (charset.toUpperCase match {
        case "UTF8" => "AL32UTF8"
        case "UTF-8" => "AL32UTF8"
        case "GBK" => "ZHS16GBK"
        case "GB2312" => "ZHS16GBK"
        case _ => "ZHS16GBK"
      }))

      // 支持多文件导入
      datFiles.split(",").foreach(x => {
        p.println("infile '%s'" format x)
      })

      p.println(loadMode)

      p.println("into table %s" format tableName)

      // 按指定字段分隔符进行分隔
      if (!splitString.equals("")) {
        p.println("fields terminated by x'%s'" format splitString)
      }

      p.println("trailing nullcols")
      p.println("(")

      // 获取字段列表
      var columnsList = configMapper.queryLoaders(tableName)

      // 如果不手动配置，就全字段
      if (columnsList.isEmpty) {
        columnsList = configMapper.queryOracleDefaultLoaders(tableName)
      }

      // 是否包含标题行
      withHead match {
        case "yes" =>
          val columnsMap = columnsList.map(x => Map(x.columnName -> x.columnFormat)).reduce(_ ++ _)
          p.print(
            Source.fromFile(datFiles, charset).bufferedReader()
              .readLine().split(RegexpEscape(Hex2String(splitString)), -1)
              .map(x => columnsMap(x.toUpperCase))
              .mkString(",")
          )
        case "no" =>
          p.print(columnsList.map(x => x.columnFormat).mkString(","))
      }

      p.println()

      p.println(")")
    }

    )
  }

  /**
    * 按照字段分隔符从文件加载数据到数据库。
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
    if (LoadMode.contains(loadMode)) {
      val uniquenID = idWorker.nextId()

      val ctlFile = uniquenID + ".ctl"
      val logFile = uniquenID + ".log"

      debug("控制文件写入：%s" format ctlFile)
      debug("加载文件列表：[%s]" format localFiles)
      wirteCtl(ctlFile = ctlFile, datFiles = localFiles, charset = charset, withHead = withHead, splitString = splitString, tableName = tableName.toUpperCase(), loadMode = loadMode)

      debug("加载日志写入：" + logFile)
      var command = "sqlldr %s control=%s log=%s %s" format(OracleUserId(), ctlFile, logFile, options)
      if (!options.contains("skip")) {
        if (CommUtil.isNotEmpty(withHead)) {
          command = command + s" skip=${
            withHead match {
              case "yes" => 1
              case _ => 0
            }
          }"
        }
      }

      val result = command !

      result == 0
    } else {
      error("错误的加载模式：" + loadMode)
      false
    }
  }
}