package com.service.visual.web.admin.modules.upms.service

import java.util

import com.baomidou.mybatisplus.service.IService
import com.service.visual.web.admin.entity.MenuNode
import com.service.visual.web.admin.modules.upms.entity.Menu

trait IMenuService extends IService[Menu] {

  /**
    * 登录时根据角色获取菜单
    *
    * @param roleIds
    * @return
    */
  def queryMenusByLoginRoles(roleIds: util.List[String]): util.List[MenuNode]
}
