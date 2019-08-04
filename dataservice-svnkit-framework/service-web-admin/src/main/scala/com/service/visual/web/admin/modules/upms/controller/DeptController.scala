package com.service.visual.web.admin.modules.upms.controller

import java.util

import com.baomidou.mybatisplus.mapper.{EntityWrapper, Wrapper}
import com.service.framework.core.utils.CommUtil
import com.service.framework.web.controller.AjaxResult
import com.service.visual.web.admin.core.web.controller.BaseController
import com.service.visual.web.admin.modules.upms.entity.Dept
import com.service.visual.web.admin.modules.upms.service.IDeptService
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation._

import scala.collection.JavaConversions._

@Controller
@RequestMapping(Array("/upms/dept"))
class DeptController extends BaseController {
  val PREFIX = "upms/dept/"
  @Autowired
  private val deptService: IDeptService = null

  @RequiresPermissions(Array("upms:dept:manage"))
  @RequestMapping(Array(""))
  def index: String = PREFIX + "dept"

  /**
    * 获取部门列表
    */
  @RequiresPermissions(Array("upms:dept:list"))
  @RequestMapping(value = Array("/list"))
  @ResponseBody
  def list(@RequestParam(required = false) deptName: String): util.List[Dept] = {
    val wrapper: Wrapper[Dept] = new EntityWrapper[Dept]().like("dept_name", deptName)
    deptService.selectList(wrapper)
  }

  /**
    * 部门新增页面
    */
  @RequiresPermissions(Array("upms:dept:add"))
  @RequestMapping(value = Array("/add/{deptId}"), method = Array(RequestMethod.GET))
  def deptAdd(@PathVariable deptId: String, model: Model): String = {
    if (deptId == "0") {
      // 添加顶级部门
      val dept = new Dept
      dept.rowKey = ""
      dept.deptName = "顶级部门"
      model.addAttribute("dept", dept)
    } else {
      val dept = deptService.selectById(deptId)
      model.addAttribute("dept", dept)
    }
    PREFIX + "add"
  }

  /**
    * 保存新增部门
    */
  @RequiresPermissions(Array("upms:dept:add"))
  @RequestMapping(value = Array("/add"), method = Array(RequestMethod.POST))
  @ResponseBody
  def deptAdd(dept: Dept): AjaxResult = {
    try {
      deptService.insert(dept)
      success
    } catch {
      case ex: Exception => failure(ex.getMessage)
    }
  }

  /**
    * 部门编辑页面
    */
  @RequiresPermissions(Array("upms:dept:edit"))
  @RequestMapping(value = Array("/edit/{deptId}"))
  def deptEdit(@PathVariable deptId: String, model: Model): String = {
    val dept = deptService.selectById(deptId)
    model.addAttribute("dept", dept)
    if (CommUtil.isNull(dept.pid) || CommUtil.isEmpty(dept.pid)) {
      model.addAttribute("parentName", "顶级部门")
    } else {
      model.addAttribute("parentName", deptService.selectById(dept.pid).deptName)
    }
    PREFIX + "edit"
  }

  /**
    * 保存修改部门
    */
  @RequiresPermissions(Array("upms:dept:edit"))
  @RequestMapping(value = Array("/edit"), method = Array(RequestMethod.POST))
  @ResponseBody
  def deptEdit(dept: Dept): AjaxResult = {
    try {
      deptService.updateById(dept)
      success
    } catch {
      case ex: Exception => failure(ex.getMessage)
    }
  }

  @RequiresPermissions(Array("upms:dept:delete"))
  @PostMapping(Array("/delete"))
  @ResponseBody
  def delete(ids: String): AjaxResult = {
    try {
      // 所有ID
      val aids = ids.split(",", -1).toList
      // 找出存在子部门的ID
      val pids = deptService.selectList(new EntityWrapper[Dept]().in("pid", aids)).map(_.pid).distinct
      // 找出不存在子部门的
      val dids = aids.filter(!pids.contains(_))
      if (CommUtil.isNotEmpty(dids)) {
        // 只删除不存在子部门的
        deptService.deleteBatchIds(dids)
      }
      success
    } catch {
      case ex: Exception => failure(ex.getMessage)
    }
  }

  /**
    * 选择部门树
    */
  @RequiresPermissions(Array("upms:dept:list"))
  @GetMapping(Array("/selectDeptTree")) def selectDeptTree(): String = PREFIX + "tree"

  /**
    * 加载所有部门列表树
    */
  @RequiresPermissions(Array("upms:dept:list"))
  @GetMapping(Array("/deptTreeData"))
  @ResponseBody def deptTreeData(): util.List[util.Map[String, AnyRef]] = deptService.selectDeptTree()

}
