package com.service.visual.web.admin.modules.upms.mapper

import java.util

import com.baomidou.mybatisplus.mapper.BaseMapper
import com.service.visual.web.admin.modules.upms.entity.Role
import org.apache.ibatis.annotations.Param

trait RoleMapper extends BaseMapper[Role] {
  /**
    * 根据角色删除权限
    *
    * @param role
    * @throws Exception
    * @return
    */
  @throws[Exception]
  def deleteRelationByRole(role: Role): Int

  /**
    * 插入角色菜单权限
    *
    * @param roleid
    * @param menuid
    */
  @throws[Exception]
  def insertRelationRoleMenu(@Param("roleid") roleid: String, @Param("menuid") menuid: String): Int

  /**
    * 根据角色获取菜单树
    *
    * @param role
    * @return
    */
  def roleMenuTreeData(role: Role): util.List[util.Map[String, AnyRef]]
}
