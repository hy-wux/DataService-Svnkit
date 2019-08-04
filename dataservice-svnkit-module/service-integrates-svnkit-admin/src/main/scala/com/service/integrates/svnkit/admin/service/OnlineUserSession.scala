package com.service.integrates.svnkit.admin.service

trait OnlineUserSession {
  /**
    * 获取在线用户的登录账号
    *
    * @return
    */
  def getOnlineUserAccount: String
}
