package com.service.framework.web.shiro

import scala.beans.BeanProperty

class ShiroUser extends Serializable {
  // 账号
  @BeanProperty
  var account: String = _

  // 角色集
  @BeanProperty
  var roleList: List[String] = _
}