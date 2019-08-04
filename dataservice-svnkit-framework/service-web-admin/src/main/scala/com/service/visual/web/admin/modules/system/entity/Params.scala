package com.service.visual.web.admin.modules.system.entity

import com.baomidou.mybatisplus.annotations.TableName
import com.service.framework.jdbc.entity.{BaseEntity, RowKey}

import scala.beans.BeanProperty

/**
  * @author 伍鲜
  *
  *         参数配置表
  */
@TableName("sys_params")
class Params extends com.service.framework.jdbc.entity.Params with BaseEntity with RowKey {

  @BeanProperty var paramName: String = _

  @BeanProperty var paramStatus: String = _

  @BeanProperty var moduleName: String = _
}