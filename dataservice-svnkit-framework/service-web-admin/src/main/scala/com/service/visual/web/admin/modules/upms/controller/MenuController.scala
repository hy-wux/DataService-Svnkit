package com.service.visual.web.admin.modules.upms.controller

import java.util

import com.baomidou.mybatisplus.mapper.{EntityWrapper, Wrapper}
import com.service.framework.core.utils.CommUtil
import com.service.framework.web.controller.AjaxResult
import com.service.visual.web.admin.core.web.controller.BaseController
import com.service.visual.web.admin.modules.upms.entity.Menu
import com.service.visual.web.admin.modules.upms.service.IMenuService
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation._

import scala.collection.JavaConversions._

@Controller
@RequestMapping(Array("/upms/menu"))
class MenuController extends BaseController {
  val PREFIX = "upms/menu/"
  @Autowired
  private val menuService: IMenuService = null

  @RequiresPermissions(Array("upms:menu:manage"))
  @RequestMapping(Array(""))
  def index: String = PREFIX + "menu"

  /**
    * 获取菜单列表
    */
  @RequiresPermissions(Array("upms:menu:list"))
  @RequestMapping(value = Array("/list"))
  @ResponseBody
  def list(@RequestParam(required = false) menuName: String): util.List[Menu] = {
    val wrapper: Wrapper[Menu] = new EntityWrapper[Menu]().like("menu_name", menuName)
    menuService.selectList(wrapper)
  }

  /**
    * 菜单新增页面
    */
  @RequiresPermissions(Array("upms:menu:add"))
  @RequestMapping(value = Array("/add/{menuId}"), method = Array(RequestMethod.GET))
  def menuAdd(@PathVariable menuId: String, model: Model): String = {
    if (menuId == "0") {
      // 添加顶级菜单
      val menu = new Menu
      menu.rowKey = ""
      menu.menuName = "顶级菜单"
      model.addAttribute("menu", menu)
    } else {
      val menu = menuService.selectById(menuId)
      model.addAttribute("menu", menu)
    }
    PREFIX + "add"
  }

  /**
    * 保存新增菜单
    */
  @RequiresPermissions(Array("upms:menu:add"))
  @RequestMapping(value = Array("/add"), method = Array(RequestMethod.POST))
  @ResponseBody
  def menuAdd(menu: Menu): AjaxResult = {
    try {
      menuService.insert(menu)
      success
    } catch {
      case ex: Exception => failure(ex.getMessage)
    }
  }

  /**
    * 菜单编辑页面
    */
  @RequiresPermissions(Array("upms:menu:edit"))
  @RequestMapping(value = Array("/edit/{menuId}"))
  def menuEdit(@PathVariable menuId: String, model: Model): String = {
    val menu = menuService.selectById(menuId)
    model.addAttribute("menu", menu)
    if (CommUtil.isNull(menu.pid) || CommUtil.isEmpty(menu.pid)) {
      model.addAttribute("parentName", "顶级菜单")
    } else {
      model.addAttribute("parentName", menuService.selectById(menu.pid).menuName)
    }
    PREFIX + "edit"
  }

  /**
    * 保存修改菜单
    */
  @RequiresPermissions(Array("upms:menu:edit"))
  @RequestMapping(value = Array("/edit"), method = Array(RequestMethod.POST))
  @ResponseBody
  def menuEdit(menu: Menu): AjaxResult = {
    try {
      menuService.updateById(menu)
      success
    } catch {
      case ex: Exception => failure(ex.getMessage)
    }
  }

  @RequiresPermissions(Array("upms:menu:delete"))
  @PostMapping(Array("/delete"))
  @ResponseBody
  def delete(ids: String): AjaxResult = {
    try {
      // 所有ID
      val aids = ids.split(",", -1).toList
      // 找出存在子菜单的ID
      val pids = menuService.selectList(new EntityWrapper[Menu]().in("pid", aids)).map(_.pid).distinct
      // 找出不存在子菜单的
      val dids = aids.filter(!pids.contains(_))
      if (CommUtil.isNotEmpty(dids)) {
        // 只删除不存在子菜单的
        menuService.deleteBatchIds(dids)
      }
      success
    } catch {
      case ex: Exception => failure(ex.getMessage)
    }
  }
}
