package com.service.visual.web.admin.modules.upms.entity

import com.baomidou.mybatisplus.activerecord.Model
import com.baomidou.mybatisplus.annotations.TableName
import com.service.framework.jdbc.entity.{BaseEntity, RowKey}

import scala.beans.BeanProperty

/**
  * @author 伍鲜
  *
  *         角色
  */
@TableName("sys_role")
class Role extends Model[Role] with BaseEntity with RowKey {

  @BeanProperty var roleCode: String = _

  @BeanProperty var roleName: String = _

  @BeanProperty var roleComment: String = _

  @BeanProperty var roleStatus: String = _

  @BeanProperty var roleRemark: String = _

  @BeanProperty var moduleName: String = _
}