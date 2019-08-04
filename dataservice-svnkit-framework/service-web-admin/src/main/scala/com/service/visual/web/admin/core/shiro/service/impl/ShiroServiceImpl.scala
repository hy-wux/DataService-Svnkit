package com.service.visual.web.admin.core.shiro.service.impl

import java.util

import com.baomidou.mybatisplus.mapper.EntityWrapper
import com.service.framework.core.utils.CommUtil
import com.service.visual.web.admin.core.common.constant.ConstantFactory
import com.service.visual.web.admin.core.shiro.entity.{Relation, ShiroUser, User}
import com.service.visual.web.admin.core.shiro.service.{ShiroMenuService, ShiroRelationService, ShiroService, ShiroUserService}
import org.apache.shiro.authc.{LockedAccountException, SimpleAuthenticationInfo, UnknownAccountException}
import org.apache.shiro.crypto.hash.Md5Hash
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConversions._

@Service
@DependsOn(Array("springContextHolder"))
@Transactional(readOnly = true)
class ShiroServiceImpl extends ShiroService {

  @Autowired
  private val userService: ShiroUserService = null

  @Autowired
  private val relationService: ShiroRelationService = null

  @Autowired
  private val menuService: ShiroMenuService = null

  /**
    * 根据账号获取登录用户
    *
    * @param account 账号
    * @return
    */
  override def user(account: String): User = {
    val user = userService.selectOne(new EntityWrapper[User]().eq("user_account", account).ne("user_status", "03"))

    // 账号不存在
    if (CommUtil.isEmpty(user)) throw new UnknownAccountException

    // 账号被冻结
    if (user.userStatus == "02") throw new LockedAccountException

    user
  }

  /**
    * 根据系统用户获取Shiro的用户
    *
    * @param user 系统用户
    * @return
    */
  override def shiroUser(user: User): ShiroUser = {
    val shiroUser: ShiroUser = new ShiroUser
    shiroUser.setId(user.rowKey)
    shiroUser.setAccount(user.userAccount)
    shiroUser.setAvatar(user.getAvatar)
    shiroUser.setDeptId(user.getDeptid)
    shiroUser.setName(user.userName)
    shiroUser.setDeptName(ConstantFactory.me.getDeptName(user.getDeptid))

    import scala.collection.JavaConversions._
    shiroUser.setRoleList(if (user.getRoleids == null) List[String]() else user.getRoleids.split(",", -1).toList)
    shiroUser.setRoleNames(if (user.getRoleids == null) List[String]() else ConstantFactory.me.getMultipleRoleName(user.getRoleids).split(",", -1).toList)

    shiroUser
  }

  /**
    * 获取Shiro的认证信息
    *
    * @param shiroUser Shiro用户
    * @param user      系统用户
    * @param realmName
    * @return
    */
  override def info(shiroUser: ShiroUser, user: User, realmName: String): SimpleAuthenticationInfo = {
    val credentials = user.userPass
    // 密码加盐处理
    val source = user.userSalt
    val credentialsSalt = new Md5Hash(source)
    new SimpleAuthenticationInfo(shiroUser, credentials, credentialsSalt, realmName)
  }

  /**
    * 获取权限列表通过角色id
    *
    * @param roleId 角色id
    */
  override def findPermissionsByRoleId(roleId: String): util.List[String] = {
    val relaions = relationService.selectList(new EntityWrapper[Relation]().eq("roleid", roleId))
    menuService.selectBatchIds(relaions.map(_.menuid)).filter(_.menuStatus == "01").map(_.permission)
  }

  /**
    * 根据角色id获取角色名称
    *
    * @param roleId 角色id
    */
  override def findRoleNameByRoleId(roleId: String): String = ConstantFactory.me.getSingleRoleName(roleId)
}
