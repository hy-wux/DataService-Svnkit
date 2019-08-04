package com.service.visual.web.admin.modules.system.controller

import com.baomidou.mybatisplus.mapper.EntityWrapper
import com.service.framework.core.utils.CommUtil
import com.service.framework.web.controller.AjaxResult
import com.service.visual.web.admin.core.web.controller.BaseController
import com.service.visual.web.admin.modules.system.entity.{DictCode, DictType}
import com.service.visual.web.admin.modules.system.service.{IDictCodeService, IDictTypeService}
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation._

import scala.collection.JavaConversions._

@Controller
@RequestMapping(Array("/system/dict/type"))
class DictTypeController extends BaseController {
  private val dictTypePrefix = "system/dict/type"
  private val dictCodePrefix = "system/dict/code"

  @Autowired private val dictTypeService: IDictTypeService = null
  @Autowired private val dictCodeService: IDictCodeService = null

  @RequiresPermissions(Array("system:dict:manage"))
  @GetMapping()
  def dictTypeIndex() = dictTypePrefix + "/list"

  /**
    * 获取字典列表
    */
  @RequiresPermissions(Array("system:dict:list"))
  @RequestMapping(value = Array("/list"))
  @ResponseBody
  def list(@RequestParam(required = false) typeName: String): Any = {
    basePageQuery[DictType](dictTypeService, List(("type_name", "like", typeName)))
  }

  /**
    * 新增字典类型
    */
  @RequiresPermissions(Array("system:dict:add"))
  @GetMapping(Array("/add"))
  def add: String = dictTypePrefix + "/add"

  /**
    * 校验字典类型
    */
  @PostMapping(Array("/checkDictTypeUnique"))
  @ResponseBody
  def checkDictTypeUnique(dictType: DictType): String = {
    var result: String = null
    if (CommUtil.isNotNull(dictType)) {
      val wrapper = new EntityWrapper[DictType]().eq("type_code", dictType.typeCode)
      if (CommUtil.isNotEmpty(dictType.rowKey)) {
        wrapper.ne("row_key", dictType.rowKey)
      }
      result = s"${dictTypeService.selectCount(wrapper)}"
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
  def addSave(dict: DictType): AjaxResult = {
    try {
      dictTypeService.insert(dict)
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
      // 查询字典类型
      val types = dictTypeService.selectBatchIds(ids.split(",", -1).toList)
      // 删除字典值
      dictCodeService.delete(new EntityWrapper[DictCode]().in("dict_type", types.map(_.typeCode)))
      // 删除字典类型
      dictTypeService.deleteBatchIds(ids.split(",", -1).toList)
      // 成功
      success
    } catch {
      case ex: Exception => failure(ex.getMessage)
    }
  }

  @RequiresPermissions(Array("system:dict:edit"))
  @GetMapping(Array("/edit/{id}"))
  def edit(@PathVariable("id") id: String, mm: ModelMap): String = {
    mm.put("dict", dictTypeService.selectById(id))
    dictTypePrefix + "/edit"
  }

  @RequiresPermissions(Array("system:dict:edit"))
  @PostMapping(Array("/edit"))
  @ResponseBody
  def editSave(dict: DictType): AjaxResult = {
    try {
      val dictType: DictType = dictTypeService.selectById(dict.rowKey)
      val dictCode: DictCode = new DictCode
      dictCode.dictType = dict.typeCode
      dictCodeService.update(dictCode, new EntityWrapper[DictCode]().eq("dict_type", dictType.typeCode))
      dictTypeService.updateById(dict)
      success
    } catch {
      case ex: Exception => failure(ex.getMessage)
    }
  }

  @RequiresPermissions(Array("system:dict:list"))
  @GetMapping(Array("/detail/{id}"))
  def detail(@PathVariable("id") id: String, mm: ModelMap): String = {
    mm.put("dict", dictTypeService.selectById(id))
    dictCodePrefix + "/list"
  }

}
