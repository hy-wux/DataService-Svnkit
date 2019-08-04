package com.service.visual.web.admin.modules.system.service.impl

import com.baomidou.mybatisplus.service.impl.ServiceImpl
import com.service.visual.web.admin.modules.system.entity.DictCode
import com.service.visual.web.admin.modules.system.mapper.DictCodeMapper
import com.service.visual.web.admin.modules.system.service.IDictCodeService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DictCodeServiceImpl extends ServiceImpl[DictCodeMapper, DictCode] with IDictCodeService {

}
