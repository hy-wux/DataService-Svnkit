package com.service.integrates.email.entity

import cn.afterturn.easypoi.excel.annotation.Excel
import com.baomidou.mybatisplus.annotations.TableName
import com.service.framework.jdbc.entity.{BaseEntity, RowKey}

import scala.beans.BeanProperty

@TableName("eds_t_email_server")
class EmailServer extends BaseEntity with RowKey {
  @Excel(name = "服务器名称", orderNum = "1")
  @BeanProperty var serverName: String = _
  @Excel(name = "邮件协议名称", orderNum = "2")
  @BeanProperty var emailProtocol: String = _
  @Excel(name = "邮件服务器主机", orderNum = "3")
  @BeanProperty var emailHost: String = _
  @Excel(name = "邮件服务器端口", orderNum = "4")
  @BeanProperty var emailPort: Int = _
}
