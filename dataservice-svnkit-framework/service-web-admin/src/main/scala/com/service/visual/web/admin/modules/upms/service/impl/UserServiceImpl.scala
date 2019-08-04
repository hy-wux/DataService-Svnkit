package com.service.visual.web.admin.modules.upms.service.impl

import com.baomidou.mybatisplus.service.impl.ServiceImpl
import com.service.visual.web.admin.modules.upms.entity.User
import com.service.visual.web.admin.modules.upms.mapper.UserMapper
import com.service.visual.web.admin.modules.upms.service.IUserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserServiceImpl extends ServiceImpl[UserMapper, User] with IUserService {
}
