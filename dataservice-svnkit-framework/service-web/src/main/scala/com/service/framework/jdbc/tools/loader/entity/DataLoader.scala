package com.service.framework.jdbc.tools.loader.entity

import scala.beans.BeanProperty

/**
  * @author 伍鲜
  */
class DataLoader {
  @BeanProperty var tableName: String = _
  @BeanProperty var columnName: String = _
  @BeanProperty var columnId: Int = _
  @BeanProperty var columnFormat: String = _
}
