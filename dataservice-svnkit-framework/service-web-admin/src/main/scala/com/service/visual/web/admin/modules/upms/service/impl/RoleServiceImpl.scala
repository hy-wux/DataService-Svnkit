package com.service.visual.web.admin.modules.upms.service.impl

import java.util

import com.baomidou.mybatisplus.service.impl.ServiceImpl
import com.service.visual.web.admin.modules.upms.entity.Role
import com.service.visual.web.admin.modules.upms.mapper.RoleMapper
import com.service.visual.web.admin.modules.upms.service.IRoleService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class RoleServiceImpl extends ServiceImpl[RoleMapper, Role] with IRoleService {
  /**
    * 插入角色
    *
    * @param role
    * @throws Exception
    * @return
    */
  @throws[Exception]
  override def insertRole(role: Role, menuIds: String): Int = {
    val result = baseMapper.insert(role)
    baseMapper.deleteRelationByRole(role)
    baseMapper.insertRelationRoleMenu(role.rowKey, menuIds.split(",").mkString("'", "','", "'"))
    result
  }

  /**
    * 更新角色
    *
    * @param role
    * @throws Exception
    * @return
    */
  @throws[Exception]
  override def updateRole(role: Role, menuIds: String): Int = {
    val result = baseMapper.updateById(role)
    baseMapper.deleteRelationByRole(role)
    baseMapper.insertRelationRoleMenu(role.rowKey, menuIds.split(",").mkString("'", "','", "'"))
    result
  }

  /**
    * 根据角色获取菜单树
    *
    * @param role
    * @return
    */
  override def roleMenuTreeData(role: Role): util.List[util.Map[String, AnyRef]] = baseMapper.roleMenuTreeData(role)
}
