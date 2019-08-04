package com.service.visual.web.admin.modules.upms.mapper

import java.util

import com.baomidou.mybatisplus.mapper.BaseMapper
import com.service.visual.web.admin.entity.MenuNode
import com.service.visual.web.admin.modules.upms.entity.Menu

trait MenuMapper extends BaseMapper[Menu] {

  /**
    * 登录时根据角色获取菜单
    *
    * @param roleIds
    * @return
    */
  def queryMenusByLoginRoles(roleIds: util.List[String]): util.List[MenuNode]
}
