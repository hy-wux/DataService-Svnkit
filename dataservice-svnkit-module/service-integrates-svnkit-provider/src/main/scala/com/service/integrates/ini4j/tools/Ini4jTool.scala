package com.service.integrates.ini4j.tools

import java.io.File

import com.service.framework.core.utils.SystemUtil
import org.ini4j.Ini

import scala.sys.process._

object Ini4jTool {
  /**
    * 保存INI文件
    *
    * @param ini
    * @param file
    */
  def saveIniFile(ini: Ini, file: File): Unit = {
    // 保存配置
    val target = new File(s"${file.getAbsolutePath}.wux")
    ini.store(target)
    // 将Unicode编码转换为中文汉字
    if (SystemUtil.isWindows) {
      // Windows系统下执行
      Seq("cmd", "/c", s"native2ascii -reverse -encoding UTF8 ${target.getAbsolutePath} ${file.getAbsolutePath}") !
    } else {
      // 非Windows系统下执行
      Seq("bash", "-c", s"native2ascii -reverse -encoding UTF8 ${target.getAbsolutePath} ${file.getAbsolutePath}") !

      // 替换转义冒号
      Seq("bash", "-c", s"sed -i 's/\\\\:/:/g' ${file.getAbsolutePath}") !
    }
    target.delete()
  }
}
