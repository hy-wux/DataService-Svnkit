package com.service.visual.web.admin.core.shiro.realm

import com.service.framework.core.component.SpringContextHolder
import com.service.framework.core.utils.CommUtil
import com.service.visual.web.admin.core.shiro.entity.{ShiroUser, User}
import com.service.visual.web.admin.core.shiro.service.ShiroService
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc._
import org.apache.shiro.authc.credential.{CredentialsMatcher, HashedCredentialsMatcher}
import org.apache.shiro.authz.{AuthorizationInfo, SimpleAuthorizationInfo}
import org.apache.shiro.realm.AuthorizingRealm
import org.apache.shiro.subject.PrincipalCollection

import scala.collection.JavaConversions._

class UserRealm extends AuthorizingRealm {
  /**
    * 权限认证
    */
  override protected def doGetAuthorizationInfo(principals: PrincipalCollection): AuthorizationInfo = {
    val shiroService: ShiroService = SpringContextHolder.getBean(classOf[ShiroService])
    val shiroUser: ShiroUser = principals.getPrimaryPrincipal.asInstanceOf[ShiroUser]
    val info = new SimpleAuthorizationInfo
    // 设置所有角色拥有的权限
    info.addStringPermissions(shiroUser.getRoleList.map(shiroService.findPermissionsByRoleId(_).filter(CommUtil.isNotEmpty(_)).toList).reduce(_ ::: _))
    // 设置所有角色名称
    info.addRoles(shiroUser.getRoleList.map(shiroService.findRoleNameByRoleId(_)).filter(CommUtil.isNotEmpty(_)))
    info
  }

  /**
    * 设置认证加密方式
    */
  override def setCredentialsMatcher(credentialsMatcher: CredentialsMatcher): Unit = {
    val md5CredentialsMatcher = new HashedCredentialsMatcher
    md5CredentialsMatcher.setHashAlgorithmName("MD5")
    md5CredentialsMatcher.setHashIterations(1024)
    super.setCredentialsMatcher(md5CredentialsMatcher)
  }

  /**
    * 登录认证
    */
  @throws[AuthenticationException]
  override protected def doGetAuthenticationInfo(authcToken: AuthenticationToken): AuthenticationInfo = {
    val shiroService: ShiroService = SpringContextHolder.getBean(classOf[ShiroService])
    // 构建用户Token
    val token: UsernamePasswordToken = authcToken.asInstanceOf[UsernamePasswordToken]
    // 根据用户名获取用户信息
    val user: User = shiroService.user(token.getUsername)
    val shiroUser: ShiroUser = shiroService.shiroUser(user)
    // 判断用户密码是否正确
    val info: SimpleAuthenticationInfo = shiroService.info(shiroUser, user, super.getName)
    info
  }

  /**
    * 清理缓存权限
    */
  def clearCachedAuthorizationInfo(): Unit = {
    this.clearCachedAuthorizationInfo(SecurityUtils.getSubject.getPrincipals)
  }
}
