package com.service.framework.jdbc.entity

import java.util.Date

import scala.beans.BeanProperty

trait BaseEntity {

  /** 创建者 */
  @BeanProperty var createPerson: String = _

  /** 创建时间 */
  @BeanProperty var createDatetime: Date = _

  /** 更新者 */
  @BeanProperty var updatePerson: String = _

  /** 更新时间 */
  @BeanProperty var updateDatetime: Date = _

}
