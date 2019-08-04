package com.service.framework.web.utils

import com.service.framework.web.shiro.ShiroUser
import org.apache.shiro.SecurityUtils
import org.apache.shiro.crypto.hash.{Md5Hash, SimpleHash}
import org.apache.shiro.session.Session
import org.apache.shiro.subject.Subject

class ShiroUtils {
  def getSubject: Subject = SecurityUtils.getSubject

  def getSession: Session = SecurityUtils.getSubject.getSession

  def getPrincipal = SecurityUtils.getSubject.getPrincipal

  def logout: Unit = SecurityUtils.getSubject.logout()

  def isUser: Boolean = getSubject != null && getSubject.getPrincipal != null

  def isGuest: Boolean = !isUser

  def getUser: ShiroUser = if (isGuest) null else getSubject.getPrincipals.getPrimaryPrincipal.asInstanceOf[ShiroUser]

  def md5(credentials: String, saltSource: String): String = new SimpleHash("MD5", credentials, new Md5Hash(saltSource), 1024).toString
}

object ShiroUtils {
  private val me = new ShiroUtils

  def getSubject: Subject = me.getSubject

  def getUser: ShiroUser = me.getUser

  def md5(credentials: String, saltSource: String): String = me.md5(credentials, saltSource)
}