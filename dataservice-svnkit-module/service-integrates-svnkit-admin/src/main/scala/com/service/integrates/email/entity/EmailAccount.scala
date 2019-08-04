package com.service.integrates.email.entity

import com.baomidou.mybatisplus.annotations.{TableField, TableName}
import com.service.framework.jdbc.entity.{BaseEntity, RowKey}

import scala.beans.BeanProperty

@TableName("eds_t_email_account")
class EmailAccount extends BaseEntity with RowKey {
  @BeanProperty var serverKey: String = _
  @TableField(exist = false)
  @BeanProperty var serverName: String = _
  @BeanProperty var accountName: String = _
  @BeanProperty var username: String = _
  @BeanProperty var password: String = _
}
