package com.service.visual.web.admin.modules.upms.service

import java.util

import com.baomidou.mybatisplus.service.IService
import com.service.visual.web.admin.modules.upms.entity.Dept

trait IDeptService extends IService[Dept] {
  /**
    * 查询部门树
    *
    * @return
    */
  def selectDeptTree(): util.List[util.Map[String, AnyRef]]

}
