package com.service.visual.web.admin.core.shiro.service

import java.util

import com.service.visual.web.admin.core.shiro.entity.{ShiroUser, User}
import org.apache.shiro.authc.SimpleAuthenticationInfo

trait ShiroService {
  /**
    * 根据账号获取登录用户
    *
    * @param account 账号
    * @return
    */
  def user(account: String): User

  /**
    * 根据系统用户获取Shiro的用户
    *
    * @param user 系统用户
    * @return
    */
  def shiroUser(user: User): ShiroUser

  /**
    * 获取权限列表通过角色id
    *
    * @param roleId 角色id
    */
  def findPermissionsByRoleId(roleId: String): util.List[String]

  /**
    * 根据角色id获取角色名称
    *
    * @param roleId 角色id
    */
  def findRoleNameByRoleId(roleId: String): String

  /**
    * 获取Shiro的认证信息
    *
    * @param shiroUser Shiro用户
    * @param user      系统用户
    * @param realmName
    * @return
    */
  def info(shiroUser: ShiroUser, user: User, realmName: String): SimpleAuthenticationInfo
}
