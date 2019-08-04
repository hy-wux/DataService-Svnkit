package com.service.integrates.email.service.impl

import com.baomidou.mybatisplus.service.impl.ServiceImpl
import com.service.integrates.email.entity.EmailServer
import com.service.integrates.email.mapper.EmailServerMapper
import com.service.integrates.email.service.IEmailServerService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MybatisEmailServerServiceImpl extends ServiceImpl[EmailServerMapper, EmailServer] with IEmailServerService {
}
