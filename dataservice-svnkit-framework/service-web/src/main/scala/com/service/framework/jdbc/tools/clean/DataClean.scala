package com.service.framework.jdbc.tools.clean

/**
  * @author 伍鲜
  *
  *         数据清理接口
  */
trait DataClean {
  /**
    * 清理数据
    *
    * @param date   批量日期
    * @param tables 需要清理的表
    */
  def cleanData(date: String, tables: Array[(String, String, Int)])
}
