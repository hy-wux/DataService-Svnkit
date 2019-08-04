package com.service.visual.web.admin.core.shiro.service.impl

import com.baomidou.mybatisplus.service.impl.ServiceImpl
import com.service.visual.web.admin.core.shiro.entity.Relation
import com.service.visual.web.admin.core.shiro.mapper.ShiroRelationMapper
import com.service.visual.web.admin.core.shiro.service.ShiroRelationService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ShiroRelationServiceImpl extends ServiceImpl[ShiroRelationMapper, Relation] with ShiroRelationService {
}
