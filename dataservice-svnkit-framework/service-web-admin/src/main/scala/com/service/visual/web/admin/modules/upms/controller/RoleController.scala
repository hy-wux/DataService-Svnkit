package com.service.visual.web.admin.modules.upms.controller

import java.util

import com.baomidou.mybatisplus.mapper.EntityWrapper
import com.service.framework.core.utils.CommUtil
import com.service.framework.web.controller.AjaxResult
import com.service.visual.web.admin.core.web.controller.BaseController
import com.service.visual.web.admin.modules.upms.entity.Role
import com.service.visual.web.admin.modules.upms.service.IRoleService
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation._

import scala.collection.JavaConversions._

@Controller
@RequestMapping(Array("/upms/role"))
class RoleController extends BaseController {
  private val PREFIX = "upms/role"

  @Autowired private val roleService: IRoleService = null

  @RequiresPermissions(Array("upms:role:manage"))
  @GetMapping()
  def roleIndex() = PREFIX + "/list"

  /**
    * 获取角色列表
    */
  @RequiresPermissions(Array("upms:role:list"))
  @RequestMapping(value = Array("/list"))
  @ResponseBody
  def list(@RequestParam(required = false) roleName: String): Any = {
    basePageQuery[Role](roleService, List(("role_name", "like", roleName)))
  }

  /**
    * 新增角色
    */
  @RequiresPermissions(Array("upms:role:add"))
  @GetMapping(Array("/add"))
  def add: String = PREFIX + "/add"

  /**
    * 校验角色
    */
  @PostMapping(Array("/checkRoleUnique"))
  @ResponseBody
  def checkRoleUnique(role: Role): String = {
    var result: String = null
    if (CommUtil.isNotNull(role)) {
      result = s"${roleService.selectCount(new EntityWrapper[Role]().eq("role_code", role.roleCode).ne("row_key", role.rowKey))}"
    } else {
      result = "0"
    }
    result
  }

  /**
    * 新增保存角色
    */
  @RequiresPermissions(Array("upms:role:add"))
  @PostMapping(Array("/add"))
  @ResponseBody
  def addSave(role: Role, menuIds: String): AjaxResult = {
    try {
      roleService.insertRole(role, menuIds)
      success
    } catch {
      case ex: Exception => failure(ex.getMessage)
    }
  }

  @RequiresPermissions(Array("upms:role:delete"))
  @PostMapping(Array("/delete"))
  @ResponseBody
  def delete(ids: String): AjaxResult = {
    try {
      roleService.deleteBatchIds(ids.split(",", -1).toList)
      success
    } catch {
      case ex: Exception => failure(ex.getMessage)
    }
  }

  @RequiresPermissions(Array("upms:role:edit"))
  @GetMapping(Array("/edit/{id}"))
  def edit(@PathVariable("id") id: String, mm: ModelMap): String = {
    mm.put("role", roleService.selectById(id))
    PREFIX + "/edit"
  }

  @RequiresPermissions(Array("upms:role:edit"))
  @PostMapping(Array("/edit"))
  @ResponseBody
  def editSave(role: Role, menuIds: String): AjaxResult = {
    try {
      roleService.updateRole(role, menuIds)
      success
    } catch {
      case ex: Exception => failure(ex.getMessage)
    }
  }

  /**
    * 加载所有菜单列表树
    */
  @RequiresPermissions(Array("upms:menu:list"))
  @GetMapping(Array("/roleMenuTreeData"))
  @ResponseBody def roleMenuTreeData(role: Role): util.List[util.Map[String, AnyRef]] = {
    if (CommUtil.isEmpty(role.rowKey)) {
      role.rowKey = ""
    }
    roleService.roleMenuTreeData(role)
  }
}
