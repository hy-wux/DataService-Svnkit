package com.service.integrates.svnkit.admin.service.impl

import com.baomidou.mybatisplus.service.impl.ServiceImpl
import com.service.integrates.svnkit.admin.entity.SvnkitRepository
import com.service.integrates.svnkit.admin.mapper.SvnkitRepositoryMapper
import com.service.integrates.svnkit.admin.service.ISvnkitRepositoryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MybatisSvnkitRepositoryServiceImpl extends ServiceImpl[SvnkitRepositoryMapper, SvnkitRepository] with ISvnkitRepositoryService {

}
