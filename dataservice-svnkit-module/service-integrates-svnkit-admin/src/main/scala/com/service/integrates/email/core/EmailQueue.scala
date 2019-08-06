package com.service.integrates.email.core

import java.util.concurrent.LinkedBlockingQueue

/**
  * @author 伍鲜
  *
  *         邮件发送队列
  */
object EmailQueue {
  private[core] val blockingQueue = new LinkedBlockingQueue[EmailContent]()
}
