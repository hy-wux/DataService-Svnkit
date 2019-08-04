package com.service.visual.web.admin.modules.upms.service.impl

import java.util

import com.baomidou.mybatisplus.service.impl.ServiceImpl
import com.service.visual.web.admin.modules.upms.entity.Dept
import com.service.visual.web.admin.modules.upms.mapper.DeptMapper
import com.service.visual.web.admin.modules.upms.service.IDeptService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DeptServiceImpl extends ServiceImpl[DeptMapper, Dept] with IDeptService {
  /**
    * 查询部门树
    *
    * @return
    */
  override def selectDeptTree(): util.List[util.Map[String, AnyRef]] = baseMapper.selectDeptTree()
}
