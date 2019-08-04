package com.service.integrates.svnkit.admin.service.impl

import com.baomidou.mybatisplus.service.impl.ServiceImpl
import com.service.integrates.svnkit.admin.entity.SvnkitUser
import com.service.integrates.svnkit.admin.mapper.SvnkitUserMapper
import com.service.integrates.svnkit.admin.service.ISvnkitUserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MybatisSvnkitUserServiceImpl extends ServiceImpl[SvnkitUserMapper, SvnkitUser] with ISvnkitUserService {

}
