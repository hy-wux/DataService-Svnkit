package com.service.framework.core.utils

import java.util.Random
import java.util.regex.{Matcher, Pattern}

/**
  * @author 伍鲜
  *
  *         字符串工具类
  */
class StringUtil {

}

/**
  * @author 伍鲜
  *
  *         字符串工具类
  */
object StringUtil {
  /**
    * 判断字符串是否包含在给定的字符串中
    *
    * @param str
    * @param args
    * @return
    */
  def inStringIgnoreCase(str: String, args: String*): Boolean = !args.filter(_.equalsIgnoreCase(str)).isEmpty

  /**
    * 驼峰命名规则转换成下划线连接方式
    *
    * @param s 驼峰规则的字符串
    * @return
    */
  def toUnderScoreCase(s: String): String = {
    if (s == null) return null
    val sb = new StringBuilder
    var upperCase = false
    for (i <- 0 until s.length) {
      val c = s.charAt(i)
      var nextUpperCase = true
      if (i < (s.length - 1)) {
        nextUpperCase = Character.isUpperCase(s.charAt(i + 1))
      }
      if ((i > 0) && Character.isUpperCase(c)) {
        if (!upperCase || !nextUpperCase) {
          sb.append("_")
        }
        upperCase = true
      } else {
        upperCase = false
      }
      sb.append(Character.toLowerCase(c))
    }
    sb.toString
  }

  /**
    * 获取指定长度的随机字符串
    *
    * @param length
    * @return
    */
  def getRandomString(length: Int): String = {
    val base = "aA0bBcC1dDeE2fFgG3hHiI4jJkK5lLmM6nNoO7pPqQ8rRsS9tTuUvVwWxXyYzZ"
    val random = new Random
    (0 until length).map(x => {
      base.charAt(random.nextInt(base.length))
    }).mkString("")
  }

  /**
    * 去掉字符串首部的指定字符串
    *
    * @param str
    * @param delim
    * @return
    */
  def trimStart(str: String, delim: String): String = {
    var ret = str
    if (CommUtil.isNotNull(str)) {
      if (ret.startsWith(delim)) {
        ret = ret.substring(delim.length)
      }
    }
    ret
  }

  /**
    * 去掉字符串尾部的指定字符串
    *
    * @param str
    * @param delim
    * @return
    */
  def trimEnd(str: String, delim: String): String = {
    var ret = str
    if (CommUtil.isNotNull(str)) {
      if (ret.endsWith(delim)) {
        ret = ret.substring(0, ret.length - delim.length)
      }
    }
    ret
  }

  /**
    * 判断是否为数字
    *
    * @param str
    * @return
    */
  def isDigital(str: String): Boolean = {
    if (CommUtil.isEmpty(str)) {
      false
    } else {
      val ptn = Pattern.compile("^(-|\\+)?\\d*$")
      val mat = ptn.matcher(str)
      mat.matches
    }
  }

  /**
    * 校验是否是数字 小数 222.33 p=5；s=2
    *
    * @param str
    * 需要校验的值
    * @param p
    * 数据总长度（不带小数点）则整数位数为5-2=3
    * @param s
    * 小数位数
    * @return
    */
  def isDigital(str: String, p: Integer, s: Integer): Boolean = {
    try {
      var l = "*"
      var ls = ""
      if (s == null) {
        ls = "(\\.[0-9]*)?"
      } else if (s == 0) {
        ls = "(\\.[0-9]{1," + s + "})?"
      }
      if (p != null) {
        if (p - s <= 1) {
          l = "{" + (p - s) + "}"
        } else {
          l = "{1," + (p - s) + "}"
        }
      }
      val r = "^-?[0-9]" + l + "+" + ls + "$"
      str.matches(r)
    } catch {
      case _: Exception =>
        false
    }
  }

  def stringFormat(sourStr: String, param: Map[String, AnyRef]): String = {
    val pattern = Pattern.compile("\\{(.*?)\\}")
    var matcher: Matcher = null
    var tagerStr = sourStr
    if (param == null) {
      return tagerStr
    }
    matcher = pattern.matcher(tagerStr)
    while (matcher.find) {
      val key = matcher.group
      val keyclone = key.substring(1, key.length - 1).trim
      val value = param.get(keyclone)
      if (value != null) {
        tagerStr = tagerStr.replace(key, value.toString)
      }
    }
    tagerStr
  }

  def stringFormat(str: String, paramArr: Array[AnyRef]): String = {
    if (paramArr == null) return str
    var retValue = ""
    val arr = paramArr.asInstanceOf[Array[String]]
    val m = Pattern.compile("\\{(\\d)\\}").matcher(str)
    while (m.find) {
      retValue = str.replace(m.group, arr(m.group(1).toInt))
    }
    retValue
  }

  /**
    * 获取字符串的长度，如果有中文，则每个中文字符计为3位
    *
    * @param str
    * 指定的字符串
    * @return 字符串的长度
    */
  def getStrLength(str: String): Int = {
    var value = str
    var valueLength = 0
    if (CommUtil.isEmpty(value)) {
      return valueLength
    }
    value = value.replaceAll("\r\n", "\n")
    val chinese = "[\u0391-\uFFE5]"
    for (i <- 0 until value.length) {
      val temp = value.substring(i, i + 1)
      if (temp.matches(chinese)) {
        valueLength += 3
      } else {
        valueLength += 1
      }
    }
    valueLength
  }
}