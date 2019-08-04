package com.service.integrates.svnkit.admin.service.impl

import com.baomidou.mybatisplus.service.impl.ServiceImpl
import com.service.integrates.svnkit.admin.entity.SvnkitServer
import com.service.integrates.svnkit.admin.mapper.SvnkitServerMapper
import com.service.integrates.svnkit.admin.service.ISvnkitServerService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MybatisSvnkitServerServiceImpl extends ServiceImpl[SvnkitServerMapper, SvnkitServer] with ISvnkitServerService {

}
