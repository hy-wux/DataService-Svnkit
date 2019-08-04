package com.service.framework.jdbc.mapper

import org.apache.ibatis.annotations._

/**
  * @author 伍鲜
  */
trait DatabaseMapper {
  @Insert(value = Array("${sql}"))
  def insert(@Param("sql") sql: String): Int

  @Delete(value = Array("${sql}"))
  def delete(@Param("sql") sql: String): Int

  @Update(value = Array("${sql}"))
  def update(@Param("sql") sql: String): Int

  @Select(value = Array("${sql}"))
  def select[T](@Param("sql") sql: String): T

  @Update(value = Array("${sql}"))
  def execute(@Param("sql") sql: String): Unit

}
