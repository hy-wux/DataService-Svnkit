package com.service.visual.web.admin.modules.upms.service

import java.util

import com.baomidou.mybatisplus.service.IService
import com.service.visual.web.admin.modules.upms.entity.Role

trait IRoleService extends IService[Role] {
  /**
    * 插入角色
    *
    * @param dict
    * @param menuIds
    * @throws Exception
    * @return
    */
  @throws[Exception]
  def insertRole(dict: Role, menuIds: String): Int

  /**
    * 更新角色
    *
    * @param dict
    * @param menuIds
    * @throws Exception
    * @return
    */
  @throws[Exception]
  def updateRole(dict: Role, menuIds: String): Int

  /**
    * 根据角色获取菜单树
    *
    * @param role
    * @return
    */
  def roleMenuTreeData(role: Role): util.List[util.Map[String, AnyRef]]
}
