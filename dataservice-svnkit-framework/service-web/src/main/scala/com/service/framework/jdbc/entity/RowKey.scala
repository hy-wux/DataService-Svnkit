package com.service.framework.jdbc.entity

import java.io.Serializable

import com.baomidou.mybatisplus.annotations.TableId
import com.baomidou.mybatisplus.enums.IdType

trait RowKey {
  /**
    * 主键
    */
  @TableId(value = "row_key", `type` = IdType.ID_WORKER)
  var rowKey: String = _

  def setRowKey(param: Any): Unit = {
    rowKey = param.toString
  }

  def getRowKey(): String = rowKey

  def pkVal: Serializable = rowKey
}