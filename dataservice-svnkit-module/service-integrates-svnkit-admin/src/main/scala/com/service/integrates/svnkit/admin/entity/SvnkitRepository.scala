package com.service.integrates.svnkit.admin.entity

import cn.afterturn.easypoi.excel.annotation.Excel
import com.baomidou.mybatisplus.annotations.TableName
import com.service.framework.jdbc.entity.{BaseEntity, RowKey}

import scala.beans.BeanProperty

@TableName("eds_t_svn_repositories")
class SvnkitRepository extends BaseEntity with RowKey {
  @Excel(name = "仓库名称", orderNum = "1")
  @BeanProperty var repositoryName: String = _
  @Excel(name = "仓库类型", replace = Array("空仓库_empty", "独立仓库_single"), orderNum = "2")
  @BeanProperty var repositoryType: String = _
  @Excel(name = "仓库描述", orderNum = "3")
  @BeanProperty var repositoryDesc: String = _
  @Excel(name = "联系人姓名", orderNum = "4")
  @BeanProperty var contactsPerson: String = _
  @Excel(name = "联系人邮件", orderNum = "5")
  @BeanProperty var contactsEmail: String = _
  @BeanProperty var serverKey: String = _
}
