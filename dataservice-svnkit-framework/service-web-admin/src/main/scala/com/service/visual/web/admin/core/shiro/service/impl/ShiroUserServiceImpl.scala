package com.service.visual.web.admin.core.shiro.service.impl

import com.baomidou.mybatisplus.service.impl.ServiceImpl
import com.service.visual.web.admin.core.shiro.entity.User
import com.service.visual.web.admin.core.shiro.mapper.ShiroUserMapper
import com.service.visual.web.admin.core.shiro.service.ShiroUserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ShiroUserServiceImpl extends ServiceImpl[ShiroUserMapper, User] with ShiroUserService {
}
