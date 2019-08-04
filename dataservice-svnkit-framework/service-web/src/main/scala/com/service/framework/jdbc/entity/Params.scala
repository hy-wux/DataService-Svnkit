package com.service.framework.jdbc.entity

import scala.beans.BeanProperty

/**
  * @author 伍鲜
  *
  *         参数配置表
  */
class Params {
  @BeanProperty var paramCode: String = _
  @BeanProperty var paramValue: String = _
}