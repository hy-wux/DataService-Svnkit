package com.service.visual.web.admin.modules.system.service.impl

import com.baomidou.mybatisplus.service.impl.ServiceImpl
import com.service.visual.web.admin.modules.system.entity.DictType
import com.service.visual.web.admin.modules.system.mapper.DictTypeMapper
import com.service.visual.web.admin.modules.system.service.IDictTypeService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DictTypeServiceImpl extends ServiceImpl[DictTypeMapper, DictType] with IDictTypeService {

}
