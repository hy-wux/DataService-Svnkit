package com.service.integrates.svnkit.admin.entity

import cn.afterturn.easypoi.excel.annotation.Excel
import com.baomidou.mybatisplus.annotations.TableName
import com.service.framework.jdbc.entity.{BaseEntity, RowKey}

import scala.beans.BeanProperty

@TableName("eds_t_svn_users")
class SvnkitUser extends BaseEntity with RowKey {
  @Excel(name = "用户账号", orderNum = "1")
  @BeanProperty var username: String = _
  @BeanProperty var password: String = _
  @Excel(name = "员工姓名", orderNum = "2")
  @BeanProperty var staffName: String = _
  @Excel(name = "员工工号", orderNum = "3")
  @BeanProperty var staffNum: String = _
  @Excel(name = "员工邮箱", orderNum = "4")
  @BeanProperty var staffEmail: String = _
  @Excel(name = "登录账号", orderNum = "5")
  @BeanProperty var loginAccount: String = _
  @BeanProperty var serverKey: String = _
}
