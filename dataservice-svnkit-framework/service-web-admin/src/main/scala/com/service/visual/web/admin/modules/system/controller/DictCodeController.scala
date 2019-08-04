package com.service.visual.web.admin.modules.system.controller

import com.baomidou.mybatisplus.mapper.EntityWrapper
import com.service.framework.core.utils.CommUtil
import com.service.framework.web.controller.AjaxResult
import com.service.visual.web.admin.core.web.controller.BaseController
import com.service.visual.web.admin.modules.system.entity.DictCode
import com.service.visual.web.admin.modules.system.service.IDictCodeService
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation._

import scala.collection.JavaConversions._

@Controller
@RequestMapping(Array("/system/dict/code"))
class DictCodeController extends BaseController {
  private val dictCodePrefix = "system/dict/code"

  @Autowired private val dictCodeService: IDictCodeService = null

  @RequiresPermissions(Array("system:dict:manage"))
  @GetMapping()
  def dictTypeIndex() = dictCodePrefix + "/list"

  /**
    * 获取字典列表
    */
  @RequiresPermissions(Array("system:dict:list"))
  @RequestMapping(value = Array("/list"))
  @ResponseBody
  def list(dictType: String, @RequestParam(required = false) codeText: String): Any = {
    basePageQuery[DictCode](dictCodeService, List(("dict_type", "eq", dictType), ("code_text", "like", codeText)))
  }

  /**
    * 新增字典类型
    */
  @RequiresPermissions(Array("system:dict:add"))
  @GetMapping(Array("/add/{dictType}"))
  def add(@PathVariable("dictType") dictType: String, mm: ModelMap): String = {
    mm.put("dictType", dictType)
    dictCodePrefix + "/add"
  }

  /**
    * 校验字典类型
    */
  @PostMapping(Array("/checkDictCodeUnique"))
  @ResponseBody
  def checkDictCodeUnique(dictCode: DictCode): String = {
    var result: String = null
    if (CommUtil.isNotNull(dictCode)) {
      val wrapper = new EntityWrapper[DictCode]().eq("dict_type", dictCode.dictType).eq("code_value", dictCode.codeValue)
      if (CommUtil.isNotEmpty(dictCode.rowKey)) {
        wrapper.ne("row_key", dictCode.rowKey)
      }
      result = s"${dictCodeService.selectCount(wrapper)}"
    } else {
      result = "0"
    }
    result
  }

  /**
    * 新增保存字典类型
    */
  @RequiresPermissions(Array("system:dict:add"))
  @PostMapping(Array("/add"))
  @ResponseBody
  def addSave(dict: DictCode): AjaxResult = {
    try {
      dictCodeService.insert(dict)
      success
    } catch {
      case ex: Exception => failure(ex.getMessage)
    }
  }

  @RequiresPermissions(Array("system:dict:delete"))
  @PostMapping(Array("/delete"))
  @ResponseBody
  def delete(ids: String): AjaxResult = {
    try {
      dictCodeService.deleteBatchIds(ids.split(",", -1).toList)
      success
    } catch {
      case ex: Exception => failure(ex.getMessage)
    }
  }

  @RequiresPermissions(Array("system:dict:edit"))
  @GetMapping(Array("/edit/{id}"))
  def edit(@PathVariable("id") id: String, mm: ModelMap): String = {
    mm.put("dict", dictCodeService.selectById(id))
    dictCodePrefix + "/edit"
  }

  @RequiresPermissions(Array("system:dict:edit"))
  @PostMapping(Array("/edit"))
  @ResponseBody
  def editSave(dict: DictCode): AjaxResult = {
    try {
      dictCodeService.updateById(dict)
      success
    } catch {
      case ex: Exception => failure(ex.getMessage)
    }
  }
}
