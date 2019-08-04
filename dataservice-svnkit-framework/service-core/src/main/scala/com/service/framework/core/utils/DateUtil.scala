package com.service.framework.core.utils

import java.text.{ParseException, SimpleDateFormat}
import java.util.{Calendar, Date}

/**
  * @author 伍鲜
  *
  *         日期处理工具类
  */
class DateUtil {

}

/**
  * @author 伍鲜
  *
  *         日期处理工具类
  */
object DateUtil {
  /**
    * 短日期格式 (yyyy-MM-dd)
    */
  val SHORT_DATE_PATTERN = "yyyy-MM-dd"
  /**
    * 短日期格式 (yyyyMMdd)
    */
  val SHORT_DATE_NO_SIGN_PATTERN = "yyyyMMdd"

  /**
    * 长日期格式 (yyyy-MM-dd HH:mm:ss)
    */
  val LONG_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss"
  /**
    * 长日期格式(yyyyMMDDHHmmss)
    */
  val LONG_DATE_NO_SIGN_PATTERN = "yyyyMMddHHmmss"
  /**
    * 时间：一分钟
    */
  val MS_ONE_MINUTE = 60 * 1000
  /**
    * 时间：一小时
    */
  val MS_ONE_HOUR = 60 * MS_ONE_MINUTE
  /**
    * 时间：一天
    */
  val MS_ONE_DAY = 24 * 60 * MS_ONE_MINUTE
  /**
    * 时间：一周
    */
  val MS_ONE_WEEK = 7 * MS_ONE_DAY
  /**
    * 时间：一个月
    */
  val MS_ONE_MONTH = 30 * MS_ONE_DAY
  /**
    * 时间：一年
    */
  val MS_ONE_YEAR = 365 * MS_ONE_DAY

  def getCurrentDate: Date = new Date

  def format(format: String, date: Date): String = new SimpleDateFormat(format).format(date)

  @throws[ParseException]
  def parse(format: String, date: String): Date = new SimpleDateFormat(format).parse(date)

  val SHORT_DATE_FORMAT = new SimpleDateFormat(SHORT_DATE_PATTERN)
  val SHORT_DATE_NO_SIGN_FORMAT = new SimpleDateFormat(SHORT_DATE_NO_SIGN_PATTERN)

  /**
    * 字符串转换日期。yyyy-MM-dd格式
    *
    * @param date
    * @throws ParseException
    * @return
    */
  @throws[ParseException]
  def parseShort(date: String): Date = SHORT_DATE_FORMAT.parse(date)

  /**
    * 获取指定时间的日历对象
    *
    * @param date 时间
    * @return 日历对象
    */
  def getCalendar(date: Date): Calendar = {
    val c = Calendar.getInstance
    c.setTime(date)
    c
  }

  /**
    * 返回输入的字符串代表的Calendar对象.
    *
    * @param str str 输入的字符串,格式=yyyy-MM-dd HH:mm:ss.
    * @return Calendar 返回代表输入字符串的Calendar对象.
    */
  def getCalendar(str: String): Calendar = {
    val cal = Calendar.getInstance
    try {
      val format = new SimpleDateFormat(LONG_DATE_PATTERN)
      val date = format.parse(str)
      cal.setTime(date)
    } catch {
      case _: Exception =>
    }
    cal
  }

  /**
    * 返回输入的字符串代表的Calendar对象.
    *
    * @param str     str 输入的字符串
    * @param pattern str 输入的字符串日期格式, 缺省为yyyy-MM-dd
    * @return Calendar 返回代表输入字符串的Calendar对象
    */
  def getCalendar(str: String, pattern: String): Calendar = {
    val cal = Calendar.getInstance
    try {
      val format = new SimpleDateFormat(if (CommUtil.isEmpty(pattern)) SHORT_DATE_PATTERN else pattern)
      val date = format.parse(str)
      cal.setTime(date)
    } catch {
      case _: Exception =>
    }
    cal
  }

  /**
    * 获取月初日期
    *
    * @param source  日期字符串
    * @param pattern 日期格式
    * @return 给定日期所在月的月初日期
    * @throws Exception
    */
  @throws[Exception]
  def getMonthStart(source: String, pattern: String): Date = {
    val c = Calendar.getInstance
    val date = new SimpleDateFormat(pattern).parse(source)
    c.setTime(date)
    c.set(Calendar.DAY_OF_MONTH, 1)
    c.getTime
  }

  /**
    * 获取月末日期
    *
    * @param source  日期字符串
    * @param pattern 日期格式
    * @return 给定日期所在月的月末日期
    * @throws Exception
    */
  @throws[Exception]
  def getMonthEnd(source: String, pattern: String): Date = {
    val c = Calendar.getInstance
    val date = new SimpleDateFormat(pattern).parse(source)
    c.setTime(date)
    c.add(Calendar.MONTH, 1)
    c.set(Calendar.DAY_OF_MONTH, 0)
    c.getTime
  }

  /**
    * 获取季初日期
    *
    * @param source  日期字符串
    * @param pattern 日期格式
    * @return 给定日期所在季的季初日期
    * @throws Exception
    */
  @throws[Exception]
  def getQuarterStart(source: String, pattern: String): Date = {
    val c = Calendar.getInstance
    val date = new SimpleDateFormat(pattern).parse(source)
    c.setTime(date)
    c.set(Calendar.MONTH, (c.get(Calendar.MONTH) / 3 + 1) * 3 - 4)
    c.add(Calendar.MONTH, 1)
    c.set(Calendar.DAY_OF_MONTH, 1)
    c.getTime
  }

  /**
    * 获取季末日期
    *
    * @param source  日期字符串
    * @param pattern 日期格式
    * @return 给定日期所在季的季末日期
    * @throws Exception
    */
  @throws[Exception]
  def getQuarterEnd(source: String, pattern: String): Date = {
    val c = Calendar.getInstance
    val date = new SimpleDateFormat(pattern).parse(source)
    c.setTime(date)
    c.set(Calendar.MONTH, ((c.get(Calendar.MONTH) / 3) + 1) * 3 - 1)
    c.add(Calendar.MONTH, 1)
    c.set(Calendar.DAY_OF_MONTH, 0)
    c.getTime
  }

  /**
    * 获取年初日期
    *
    * @param source  日期字符串
    * @param pattern 日期格式
    * @return 给定日期所在年的年初日期
    * @throws Exception
    */
  @throws[Exception]
  def getYearStart(source: String, pattern: String): Date = {
    val c = Calendar.getInstance
    val date = new SimpleDateFormat(pattern).parse(source)
    c.setTime(date)
    c.set(Calendar.DAY_OF_YEAR, 1)
    c.getTime
  }

  /**
    * 获取年末日期
    *
    * @param source  日期字符串
    * @param pattern 日期格式
    * @return 给定日期所在年的年末日期
    * @throws Exception
    */
  @throws[Exception]
  def getYearEnd(source: String, pattern: String): Date = {
    val c = Calendar.getInstance
    val date = new SimpleDateFormat(pattern).parse(source)
    c.setTime(date)
    c.add(Calendar.YEAR, 1)
    c.set(Calendar.DAY_OF_YEAR, 0)
    c.getTime
  }

  /**
    * 按照短日期格式(yyyy-MM-dd)转换日期对象为字符串型日期
    *
    * @param date 日期对象
    * @return 字符串型日期
    */
  def formatShort(date: Date): String = format(date, SHORT_DATE_PATTERN)

  /**
    * 按照短日期格式(yyyyMMdd)转换日期对象为字符串型日期
    *
    * @param date 日期对象
    * @return 字符串型日期
    */
  def formatShortNoSign(date: Date): String = format(date, SHORT_DATE_NO_SIGN_PATTERN)

  /**
    * 按照长日期格式(yyyy-MM-dd HH:mm:ss)转换日期对象为字符串型日期
    *
    * @param date 日期对象
    * @return 字符串型日期
    */
  def formatLong(date: Date): String = format(date, LONG_DATE_PATTERN)

  /**
    * 按照长日期格式(yyyyMMddHHmmss)转换日期对象为字符串型日期
    *
    * @param date 日期对象
    * @return 字符串型日期
    */
  def formatLongNoSign(date: Date): String = format(date, LONG_DATE_NO_SIGN_PATTERN)

  /**
    * 将Date数据类型转换为特定的格式, 如格式为null, 则使用缺省格式yyyy-MM-dd.
    *
    * @param day       day 日期
    * @param toPattern toPattern 要转换成的日期格式
    * @return String 返回日期字符串
    */
  def format(day: Date, toPattern: String): String = {
    if (day != null) {
      try {
        (if (CommUtil.isNotEmpty(toPattern)) {
          new SimpleDateFormat(toPattern)
        } else {
          new SimpleDateFormat(SHORT_DATE_PATTERN)
        }).format(day)
      } catch {
        case _: Exception =>
          null
      }
    } else {
      null
    }
  }
}