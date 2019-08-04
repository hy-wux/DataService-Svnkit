package com.service.framework.jdbc.tools.loader

/**
  * @author 伍鲜
  *
  *         从文件加载数据
  */
trait Loader4Text {
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
  def dataLoader(localFiles: String, charset: String = "UTF-8", withHead: String = "no", splitString: String = "", tableName: String, loadMode: String = "", options: String): Boolean
}