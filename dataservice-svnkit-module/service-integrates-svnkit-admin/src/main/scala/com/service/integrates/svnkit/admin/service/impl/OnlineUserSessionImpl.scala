package com.service.integrates.svnkit.admin.service.impl

import com.service.framework.web.utils.ShiroUtils
import com.service.integrates.svnkit.admin.service.OnlineUserSession

/**
  * 获取在线用户的默认实现
  */
class OnlineUserSessionImpl extends OnlineUserSession {
  /**
    * 获取在线用户的登录账号
    *
    * @return
    */
  override def getOnlineUserAccount: String = {
    try {
      ShiroUtils.getUser.getAccount
    } catch {
      case _: Throwable => "admin"
    }
  }
}
