package com.service.framework.core

import java.io._
import java.net.{InetAddress, NetworkInterface}
import java.util.Properties

import scala.io.Source

/**
  * @author 伍鲜
  *
  *         公共方法
  */
trait PubFunction {
  private[this] val regReplace = Map("|" -> "\\|", "^" -> "\\^", "*" -> "\\*", "+" -> "\\+", "$" -> "\\$", "?" -> "\\?", "." -> "\\.")

  /**
    * 写入文件。
    *
    * @param file   目标文件
    * @param append [追加true | 覆盖false]
    * @param op     写入操作
    */
  def WriteToFile(file: File, append: Boolean)(op: PrintWriter => Unit): Unit = {
    val fw = new FileWriter(file, append)
    val pw = new PrintWriter(fw)
    try {
      op(pw)
    } finally {
      try {
        pw.flush()
        fw.flush()
      } finally {
        pw.close()
        fw.close()
      }
    }
  }

  /**
    * 写入文件
    *
    * @param file    目标文件
    * @param append  [追加true | 覆盖false]
    * @param charset 字符集
    * @param op      写入操作
    */
  def WriteToFile(file: File, append: Boolean, charset: String = "UTF-8")(op: BufferedWriter => Unit): Unit = {
    val fos = new FileOutputStream(file, append)
    val osw = new OutputStreamWriter(fos, charset)
    val bw = new BufferedWriter(osw)
    try {
      op(bw)
    } finally {
      try {
        bw.flush()
        osw.flush()
        fos.flush()
      } finally {
        bw.close()
        osw.close()
        fos.close()
      }
    }
  }

  /**
    * 获取资源文件内容
    *
    * @param file 资源文件名称
    * @return
    */
  def ContentFromFile(file: String): Iterator[String] = {
    var it: Iterator[String] = Iterator()

    val files = Thread.currentThread().getContextClassLoader().getResources(file)
    while (files.hasMoreElements()) {
      try {
        it = it.++(Source.fromFile(files.nextElement().getFile).getLines())
      } catch {
        case ex: Exception => ex.printStackTrace()
      }
    }

    it
  }

  /**
    * 获取配置文件信息
    *
    * @param file 配置文件
    * @return
    */
  def PropertiesFromFile(file: String): Properties = {
    val props: Properties = new Properties()
    val files = Thread.currentThread().getContextClassLoader().getResources(file)
    var in: InputStream = null
    while (files.hasMoreElements()) {
      try {
        in = files.nextElement().openStream()
        props.load(in)
      } finally {
        try {
          in.close()
        } catch {
          case ex: Exception => /* ignore */ ex.toString()
        }
      }
    }
    props
  }

  /**
    * 十六进制转换成字符串
    *
    * @param hex 十六进制编码
    * @return
    */
  def Hex2String(hex: String): String = {
    val builder: StringBuilder = new StringBuilder()

    val pattern = "[^0-9a-fA-F]".r

    if (hex.length % 2 == 0 && "".equals(pattern findFirstIn hex getOrElse (""))) {
      for (i <- 0 to hex.length / 2 - 1) {
        builder.append(Integer.parseInt(hex.substring(i * 2, (i + 1) * 2), 16).toChar)
      }
      builder.toString()
    } else {
      throw new Exception("非法的16进制数据")
    }
  }

  /**
    * 十六进制加前缀
    *
    * @param hex 十六进制编码
    * @return
    */
  def HexAddPrefix(hex: String): String = {
    var builder: StringBuilder = new StringBuilder()

    var pattern = "[^0-9a-fA-F]".r

    if (hex.length % 2 == 0 && "".equals(pattern findFirstIn hex getOrElse (""))) {
      for (i <- 0 to hex.length / 2 - 1) {
        builder.append("0x").append(hex.substring(i * 2, (i + 1) * 2))
      }
      builder.toString()
    } else {
      throw new Exception("非法的16进制数据")
    }
  }

  /**
    * 将正则表达式中的特殊字符转义
    *
    * @param regexp 待转义的正则表达式
    * @return 将特殊字符转义后的字符串
    */
  def RegexpEscape(regexp: String): String = {
    var result = regexp
    regReplace.keySet.foreach(key => result = result.replace(key, regReplace(key)))
    result
  }

  /**
    * 将字符串补齐指定的长度，不足的在左侧填充指定的字符串
    *
    * @param src    源字符串
    * @param len    补齐的长度
    * @param append 填充的字符串
    * @return 返回指定长度的字符串，源字符串长度大于指定长度则截取指定的长度，不足则在左侧填充指定的字符串
    */
  def LAppend(src: String, len: Int, append: String): String = {
    if (len <= src.length) {
      src.substring(0, len)
    } else {
      var result = new StringBuilder()
      for (i <- 1 to len) {
        result.append(append)
      }
      result.substring(0, len - src.length) + src
    }
  }

  /**
    * 将字符串补齐指定的长度，不足的在右侧填充指定的字符串
    *
    * @param src    源字符串
    * @param len    补齐的长度
    * @param append 填充的字符串
    * @return 返回指定长度的字符串，源字符串长度大于指定长度则截取指定的长度，不足则在右侧填充指定的字符串
    */
  def RAppend(src: String, len: Int, append: String): String = {
    if (len <= src.length) {
      src.substring(0, len)
    } else {
      var result = new StringBuilder()
      for (i <- 1 to len) {
        result.append(append)
      }
      src + result.substring(0, len - src.length)
    }
  }

  /**
    * 获取本机地址列表
    *
    * @return
    */
  def getHostAddress: List[String] = {
    var ips: List[String] = List()
    try {
      val ni = NetworkInterface.getNetworkInterfaces
      while (ni.hasMoreElements) {
        val eia = ni.nextElement.getInetAddresses
        while (eia.hasMoreElements) {
          ips = ips.::(eia.nextElement.getHostAddress)
        }
      }
    } catch {
      case ex: Exception =>
        ex.printStackTrace()
    }
    ips
  }

  /**
    * 获取主机名称
    *
    * @return
    */
  def getHostName: String = {
    try {
      (InetAddress.getLocalHost).getHostName
    } catch {
      case ex: Exception =>
        ex.printStackTrace()
        "unknown"
    }
  }

  /**
    * 删除文件
    *
    * @param filePath 文件路径
    */
  def deleteFile(filePath: String): Unit = {
    try {
      val file = new File(filePath)
      if (file.exists()) {
        if (file.isFile) {
          file.delete()
        } else {
          file.listFiles().map(_.getAbsolutePath).foreach(deleteFile)
          file.delete()
        }
      }
    } catch {
      case ex: Exception =>
        ex.printStackTrace()
    }
  }
}

/**
  * @author 伍鲜
  *
  *         扩展方法
  */
object PubFunction {
  implicit def toListHelper[A](l: List[A]) = new {
    def +?[B <: A](item: Option[B]) = item match {
      case Some(b) => b :: l
      case None => l
    }
  }
}