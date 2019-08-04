package com.service.integrates.svnkit.admin.entity

import cn.afterturn.easypoi.excel.annotation.Excel
import com.baomidou.mybatisplus.annotations.{TableField, TableName}
import com.service.framework.jdbc.entity.{BaseEntity, RowKey}

import scala.beans.BeanProperty

@TableName("eds_t_svn_servers")
class SvnkitServer extends BaseEntity with RowKey {
  @Excel(name = "服务器名称", orderNum = "1")
  @BeanProperty var serverName: String = _
  @Excel(name = "微服务名称", orderNum = "2")
  @BeanProperty var serviceName: String = _
  @Excel(name = "服务器路径", orderNum = "3")
  @BeanProperty var serverAddress: String = _
  @Excel(name = "管理员名称", orderNum = "4")
  @BeanProperty var adminName: String = _
  @BeanProperty var adminAccount: String = _
  @TableField(exist = false)
  @BeanProperty var adminAccountDesc: String = _
  @BeanProperty var sendEmail: String = _
  @TableField(exist = false)
  @BeanProperty var sendEmailDesc: String = _
}
