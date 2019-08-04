package com.service.framework.jdbc.tools.export

import java.io.{File, FileInputStream, FileReader, LineNumberReader}

import com.service.framework.core.PubFunction
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.IOUtils

/**
  * @author 伍鲜
  *
  *         导出数据到文本文件
  */
trait Export2Text extends PubFunction {
  /**
    * 按照指定字段分隔符，从数据库卸载数据到文件。
    *
    * @param query       查询语句
    * @param filePath    数据文件的绝对路径
    * @param charset     字符集
    * @param withHead    是否包含标题行
    * @param splitString 字段分隔符
    * @return 卸载成功返回true，否则返回false
    */
  def export(query: String, filePath: String, charset: String, withHead: String, splitString: String): Boolean

  /**
    * 按照指定字段长度，从数据库导出数据到文件。
    *
    * @param query    查询语句
    * @param filePath 数据文件的绝对路径
    * @param charset  字符集
    * @param withHead 是否包含标题行
    * @return 导出成功返回true，否则返回false
    */
  def export(query: String, filePath: String, charset: String, withHead: String): Boolean

  /**
    * 生成文件的校验文件
    *
    * @param splitString 字段分隔符
    * @param filePath    需要生成校验文件的文件
    */
  def createCheckFile(filePath: String, splitString: String): Unit = {
    val fis = new FileInputStream(filePath)
    // 获取MD5值
    val md5 = DigestUtils.md5Hex(IOUtils.toByteArray(fis))
    // 获取行数
    val fileReader = new FileReader(filePath)
    val lineNumberReader = new LineNumberReader(fileReader)
    lineNumberReader.skip(Long.MaxValue)
    val row = lineNumberReader.getLineNumber()
    // 获取大小
    val size = fis.getChannel.size()
    lineNumberReader.close()
    fileReader.close()
    fis.close()
    // 写入文件
    WriteToFile(new File(filePath.substring(0, filePath.lastIndexOf(".")) + ".chk"), false)(p => {
      p.println(Array(row, size, md5).mkString(Hex2String(splitString)))
    })
  }
}