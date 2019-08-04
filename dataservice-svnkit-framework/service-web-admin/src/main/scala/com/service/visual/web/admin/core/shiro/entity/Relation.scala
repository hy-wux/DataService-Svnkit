package com.service.visual.web.admin.core.shiro.entity

import com.baomidou.mybatisplus.annotations.TableName

import scala.beans.BeanProperty

@TableName("sys_relation")
class Relation {
  @BeanProperty var roleid: String = _
  @BeanProperty var menuid: String = _
}
