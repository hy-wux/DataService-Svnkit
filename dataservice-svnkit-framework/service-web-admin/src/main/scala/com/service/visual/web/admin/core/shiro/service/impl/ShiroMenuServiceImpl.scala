package com.service.visual.web.admin.core.shiro.service.impl

import com.baomidou.mybatisplus.service.impl.ServiceImpl
import com.service.visual.web.admin.core.shiro.entity.Menu
import com.service.visual.web.admin.core.shiro.mapper.ShiroMenuMapper
import com.service.visual.web.admin.core.shiro.service.ShiroMenuService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ShiroMenuServiceImpl extends ServiceImpl[ShiroMenuMapper, Menu] with ShiroMenuService {
}
