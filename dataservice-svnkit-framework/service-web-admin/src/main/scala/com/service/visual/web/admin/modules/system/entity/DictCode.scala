package com.service.visual.web.admin.modules.system.entity

import com.baomidou.mybatisplus.annotations.TableName
import com.service.framework.jdbc.entity.{BaseEntity, RowKey}

import scala.beans.BeanProperty

/**
  * @author 伍鲜
  *
  *         数据字典数据
  */
@TableName("sys_dict_code")
class DictCode extends com.service.framework.jdbc.entity.DictCode with BaseEntity with RowKey {

  @BeanProperty var codeComment: String = _

  @BeanProperty var dictType: String = _

  @BeanProperty var sortNumber: String = _

  @BeanProperty var cssStyle: String = _

  @BeanProperty var listStyle: String = _

  @BeanProperty var isDefault: String = _

  @BeanProperty var codeStatus: String = _

  @BeanProperty var moduleName: String = _
}