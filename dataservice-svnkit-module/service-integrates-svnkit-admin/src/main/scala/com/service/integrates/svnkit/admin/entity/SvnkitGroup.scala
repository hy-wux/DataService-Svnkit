package com.service.integrates.svnkit.admin.entity

import cn.afterturn.easypoi.excel.annotation.Excel
import com.baomidou.mybatisplus.annotations.TableName
import com.service.framework.jdbc.entity.{BaseEntity, RowKey}

import scala.beans.BeanProperty

@TableName("eds_t_svn_groups")
class SvnkitGroup extends BaseEntity with RowKey {
  @Excel(name = "组别名称", orderNum = "1")
  @BeanProperty var groupName: String = _
  @Excel(name = "组别描述", orderNum = "2")
  @BeanProperty var groupDesc: String = _
  @BeanProperty var serverKey: String = _
}
