package com.service.visual.web.admin.modules.upms.mapper

import java.util

import com.baomidou.mybatisplus.mapper.BaseMapper
import com.service.visual.web.admin.modules.upms.entity.Dept

trait DeptMapper extends BaseMapper[Dept] {
  /**
    * 查询部门树
    *
    * @return
    */
  def selectDeptTree(): util.List[util.Map[String, AnyRef]]
}
