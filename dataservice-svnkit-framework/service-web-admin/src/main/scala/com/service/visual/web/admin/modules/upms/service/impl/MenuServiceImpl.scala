package com.service.visual.web.admin.modules.upms.service.impl

import java.util

import com.baomidou.mybatisplus.service.impl.ServiceImpl
import com.service.visual.web.admin.entity.MenuNode
import com.service.visual.web.admin.modules.upms.entity.Menu
import com.service.visual.web.admin.modules.upms.mapper.MenuMapper
import com.service.visual.web.admin.modules.upms.service.IMenuService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MenuServiceImpl extends ServiceImpl[MenuMapper, Menu] with IMenuService {

  /**
    * 登录时根据角色获取菜单
    *
    * @param roleIds
    * @return
    */
  override def queryMenusByLoginRoles(roleIds: util.List[String]): util.List[MenuNode] = baseMapper.queryMenusByLoginRoles(roleIds)
}
