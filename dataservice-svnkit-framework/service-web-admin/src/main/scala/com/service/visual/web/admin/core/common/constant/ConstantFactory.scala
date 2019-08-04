package com.service.visual.web.admin.core.common.constant

import com.service.framework.core.component.SpringContextHolder
import com.service.framework.core.logs.Logging
import com.service.framework.core.utils.CommUtil
import com.service.visual.web.admin.modules.upms.mapper.{DeptMapper, RoleMapper}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component

trait IConstantFactory {
  /**
    * 通过角色获取角色名称
    */
  def getMultipleRoleName(roleIds: String): String

  /**
    * 通过角色获取角色名称
    */
  def getSingleRoleName(roleId: String): String

  /**
    * 获取部门名称
    */
  def getDeptName(deptId: String): String
}

@Component
@DependsOn(Array("springContextHolder"))
class ConstantFactory extends IConstantFactory with Logging {
  @Autowired val roleMapper: RoleMapper = null
  @Autowired val deptMapper: DeptMapper = null

  /**
    * 通过角色获取角色名称
    */
  @Cacheable(value = Array("CONSTANT"), key = "'MULTIPLE_ROLES_NAME_'+#roleIds")
  override def getMultipleRoleName(roleIds: String): String = roleIds.split(",", -1).map(roleId => getSingleRoleName(roleId)).filter(_ != "").mkString(",")

  /**
    * 通过角色获取角色名称
    */
  @Cacheable(value = Array("CONSTANT"), key = "'SINGLE_ROLE_NAME_'+#roleId")
  override def getSingleRoleName(roleId: String): String = {
    val roleObj = roleMapper.selectById(roleId)
    if (CommUtil.isNotEmpty(roleObj)) roleObj.roleName
    else ""
  }

  /**
    * 获取部门名称
    */
  @Cacheable(value = Array("CONSTANT"), key = "'DEPT_NAME_'+#deptId")
  override def getDeptName(deptId: String): String = {
    val dept = deptMapper.selectById(deptId)
    if (CommUtil.isNotEmpty(dept) && CommUtil.isNotEmpty(dept.deptName)) dept.deptName
    else ""
  }
}

object ConstantFactory {
  def me: IConstantFactory = SpringContextHolder.getBean("constantFactory")
}