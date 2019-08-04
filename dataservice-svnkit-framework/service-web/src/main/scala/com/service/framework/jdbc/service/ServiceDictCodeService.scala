package com.service.framework.jdbc.service

import java.util

import com.service.framework.jdbc.entity.DictCode

trait ServiceDictCodeService[T <: DictCode] {

  /**
    * 根据字典类型获取字典列表
    *
    * @param dictType
    * @return
    */
  def selectDictCodeByType(dictType: String): util.List[T]
}
