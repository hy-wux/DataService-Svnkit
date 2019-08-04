package com.service.integrates.email.service.impl

import com.baomidou.mybatisplus.service.impl.ServiceImpl
import com.service.integrates.email.entity.EmailAccount
import com.service.integrates.email.mapper.EmailAccountMapper
import com.service.integrates.email.service.IEmailAccountService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MybatisEmailAccountServiceImpl extends ServiceImpl[EmailAccountMapper, EmailAccount] with IEmailAccountService {
}
