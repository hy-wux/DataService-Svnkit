package com.service.visual.web.admin.modules.system.service.impl

import com.baomidou.mybatisplus.service.impl.ServiceImpl
import com.service.visual.web.admin.modules.system.entity.Params
import com.service.visual.web.admin.modules.system.mapper.ParamsMapper
import com.service.visual.web.admin.modules.system.service.IParamsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ParamsServiceImpl extends ServiceImpl[ParamsMapper, Params] with IParamsService {

}
