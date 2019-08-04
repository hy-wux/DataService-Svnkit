package com.service.visual.web.admin.core.shiro.entity

import java.util

import scala.beans.BeanProperty

class ShiroUser extends com.service.framework.web.shiro.ShiroUser {
  // 主键ID
  @BeanProperty
  var id: String = _

  // 姓名
  @BeanProperty
  var name: String = _

  // 头像
  @BeanProperty
  var avatar: String = _

  // 部门ID
  @BeanProperty
  var deptId: String = _

  // 部门名称
  @BeanProperty
  var deptName: String = _

  // 角色名称集
  @BeanProperty
  var roleNames: util.List[String] = _
}