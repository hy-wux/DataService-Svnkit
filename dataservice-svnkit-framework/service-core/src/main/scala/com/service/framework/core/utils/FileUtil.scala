package com.service.framework.core.utils

import java.io.{File, FileInputStream}

/**
  * @author 伍鲜
  *
  *         文件处理工具类
  */
class FileUtil {

}

/**
  * @author 伍鲜
  *
  *         文件处理工具类
  */
object FileUtil {
  def toBytes(file: File): Array[Byte] = {
    val fis = new FileInputStream(file)
    try {
      val len = file.length()
      val arr = new Array[Byte](len.toInt)
      fis.read(arr)
      arr
    } finally {
      fis.close
    }
  }
}
