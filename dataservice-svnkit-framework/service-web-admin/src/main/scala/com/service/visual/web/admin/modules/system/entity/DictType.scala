package com.service.visual.web.admin.modules.system.entity

import com.baomidou.mybatisplus.activerecord.Model
import com.baomidou.mybatisplus.annotations.TableName
import com.service.framework.jdbc.entity.{BaseEntity, RowKey}

import scala.beans.BeanProperty

/**
  * @author 伍鲜
  *
  *         数据字典类型
  */
@TableName("sys_dict_type")
class DictType extends Model[DictType] with BaseEntity with RowKey {

  @BeanProperty var typeCode: String = _

  @BeanProperty var typeName: String = _

  @BeanProperty var typeComment: String = _

  @BeanProperty var typeStatus: String = _

  @BeanProperty var typeRemark: String = _

  @BeanProperty var moduleName: String = _
}