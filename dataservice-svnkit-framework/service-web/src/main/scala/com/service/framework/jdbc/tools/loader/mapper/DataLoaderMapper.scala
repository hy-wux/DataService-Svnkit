package com.service.framework.jdbc.tools.loader.mapper

import java.util

import com.service.framework.jdbc.tools.loader.entity.DataLoader
import org.apache.ibatis.annotations.{Param, Select}

/**
  * @author 伍鲜
  */
trait DataLoaderMapper {
  @Select(value = Array(
    """select table_name     as tableName
             ,column_name    as columnName
             ,column_id      as columnId
             ,column_format  as columnFormat
         from eds_t_data_loader_list
        where table_name = #{tableName}
        order by column_id"""))
  def queryLoaders(@Param("tableName") tableName: String): util.List[DataLoader]

  @Select(value = Array(
    """select table_name     as tableName
             ,column_name    as columnName
             ,column_id      as columnId
             ,column_name || (CASE WHEN DATA_TYPE = 'DATE' THEN ' DATE ''YYYY-MM-DD HH24:MI:SS'''
                                   ELSE ''
                               END)   as columnFormat
         from user_tab_columns
        where table_name = #{tableName}
        order by column_id"""))
  def queryOracleDefaultLoaders(@Param("tableName") tableName: String): util.List[DataLoader]
}
