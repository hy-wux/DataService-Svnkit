package com.service.framework.jdbc.service

import com.service.framework.jdbc.entity.Params

trait ServiceParamsService[T <: Params] {

  /**
    * 根据key值获取value
    *
    * @param paramCode
    * @return
    */
  def getParamValue(paramCode: String): T
}
