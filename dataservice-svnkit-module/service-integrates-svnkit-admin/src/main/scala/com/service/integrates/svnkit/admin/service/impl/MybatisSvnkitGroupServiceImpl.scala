package com.service.integrates.svnkit.admin.service.impl

import com.baomidou.mybatisplus.service.impl.ServiceImpl
import com.service.integrates.svnkit.admin.entity.SvnkitGroup
import com.service.integrates.svnkit.admin.mapper.SvnkitGroupMapper
import com.service.integrates.svnkit.admin.service.ISvnkitGroupService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MybatisSvnkitGroupServiceImpl extends ServiceImpl[SvnkitGroupMapper, SvnkitGroup] with ISvnkitGroupService {

}
