package com.service.integrates.email.entity

import scala.beans.BeanProperty

class EmailSender {
  @BeanProperty var accountKey: String = _
  @BeanProperty var emailTo: String = _
  @BeanProperty var subject: String = _
  @BeanProperty var emailContent: String = _
}
