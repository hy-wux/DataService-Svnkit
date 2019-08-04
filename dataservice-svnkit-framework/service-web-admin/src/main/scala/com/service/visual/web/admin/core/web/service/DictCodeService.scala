package com.service.visual.web.admin.core.web.service

import java.util

import com.baomidou.mybatisplus.mapper.EntityWrapper
import com.service.framework.jdbc.service.ServiceDictCodeService
import com.service.visual.web.admin.modules.system.entity.DictCode
import com.service.visual.web.admin.modules.system.service.IDictCodeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service("dictCode")
class DictCodeService extends ServiceDictCodeService[DictCode]{
  @Autowired private val dictCodeService: IDictCodeService = null

  /**
    * 根据字典类型获取字典列表
    *
    * @param dictType
    * @return
    */
  def selectDictCodeByType(dictType: String): util.List[DictCode] = dictCodeService.selectList(new EntityWrapper[DictCode]().eq("dict_type", dictType).eq("code_status", "01").orderBy("sort_number"))
}
