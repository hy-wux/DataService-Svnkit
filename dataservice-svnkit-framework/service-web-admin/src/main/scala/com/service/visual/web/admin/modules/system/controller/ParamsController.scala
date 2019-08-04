package com.service.visual.web.admin.modules.system.controller

import com.baomidou.mybatisplus.mapper.EntityWrapper
import com.service.framework.core.utils.CommUtil
import com.service.framework.web.controller.AjaxResult
import com.service.visual.web.admin.core.web.controller.BaseController
import com.service.visual.web.admin.modules.system.entity.Params
import com.service.visual.web.admin.modules.system.service.IParamsService
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation._

import scala.collection.JavaConversions._

@Controller
@RequestMapping(Array("/system/params"))
class ParamsController extends BaseController {
  private val paramsPrefix = "system/params"

  @Autowired private val paramsService: IParamsService = null

  @RequiresPermissions(Array("system:params:manage"))
  @GetMapping()
  def index: String = paramsPrefix + "/list"

  /**
    * 获取参数列表
    */
  @RequiresPermissions(Array("system:params:list"))
  @RequestMapping(value = Array("/list"))
  @ResponseBody
  def list(@RequestParam(required = false) paramsQuery: String): Any = {
    basePageQuery[Params](paramsService, new EntityWrapper[Params]().like("param_code", paramsQuery).or().like("param_name", paramsQuery).or().like("param_value", paramsQuery))
  }

  /**
    * 新增参数
    */
  @RequiresPermissions(Array("system:params:add"))
  @GetMapping(Array("/add"))
  def add: String = paramsPrefix + "/add"

  /**
    * 校验参数
    */
  @PostMapping(Array("/checkParamsUnique"))
  @ResponseBody
  def checkParamsUnique(params: Params): String = {
    var result: String = null
    if (CommUtil.isNotNull(params)) {
      val wrapper = new EntityWrapper[Params]().eq("param_code", params.paramCode)
      if (CommUtil.isNotEmpty(params.rowKey)) {
        wrapper.ne("row_key", params.rowKey)
      }
      result = s"${paramsService.selectCount(wrapper)}"
    } else {
      result = "0"
    }
    result
  }

  /**
    * 新增保存参数
    */
  @RequiresPermissions(Array("system:params:add"))
  @PostMapping(Array("/add"))
  @ResponseBody
  def addSave(params: Params): AjaxResult = {
    try {
      paramsService.insert(params)
      success
    } catch {
      case ex: Exception => failure(ex.getMessage)
    }
  }

  @RequiresPermissions(Array("system:params:delete"))
  @PostMapping(Array("/delete"))
  @ResponseBody
  def delete(ids: String): AjaxResult = {
    try {
      // 删除参数
      paramsService.deleteBatchIds(ids.split(",", -1).toList)
      // 成功
      success
    } catch {
      case ex: Exception => failure(ex.getMessage)
    }
  }

  @RequiresPermissions(Array("system:params:edit"))
  @GetMapping(Array("/edit/{id}"))
  def edit(@PathVariable("id") id: String, mm: ModelMap): String = {
    mm.put("params", paramsService.selectById(id))
    paramsPrefix + "/edit"
  }

  @RequiresPermissions(Array("system:params:edit"))
  @PostMapping(Array("/edit"))
  @ResponseBody
  def editSave(params: Params): AjaxResult = {
    try {
      paramsService.updateById(params)
      success
    } catch {
      case ex: Exception => failure(ex.getMessage)
    }
  }
}
