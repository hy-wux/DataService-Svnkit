package com.service.visual.web.admin.core.utils

import com.service.visual.web.admin.core.shiro.entity.ShiroUser
import com.service.visual.web.admin.core.shiro.realm.UserRealm
import com.service.visual.web.admin.modules.upms.entity.User
import org.apache.shiro.SecurityUtils
import org.apache.shiro.mgt.RealmSecurityManager
import org.apache.shiro.subject.{SimplePrincipalCollection, Subject}

class ShiroUtils extends com.service.framework.web.utils.ShiroUtils {
  def setUser(user: User): Unit = getSubject.runAs(new SimplePrincipalCollection(user, getSubject.getPrincipals.getRealmNames.iterator.next))

  override def getUser: ShiroUser = if (isGuest) null else getSubject.getPrincipals.getPrimaryPrincipal.asInstanceOf[ShiroUser]

  def clearCachedAuthorizationInfo(): Unit = SecurityUtils.getSecurityManager.asInstanceOf[RealmSecurityManager].getRealms.iterator.next.asInstanceOf[UserRealm].clearCachedAuthorizationInfo()
}

object ShiroUtils {
  private val me = new ShiroUtils

  def getSubject: Subject = me.getSubject

  def getUser: ShiroUser = me.getUser

  def md5(credentials: String, saltSource: String): String = me.md5(credentials, saltSource)
}