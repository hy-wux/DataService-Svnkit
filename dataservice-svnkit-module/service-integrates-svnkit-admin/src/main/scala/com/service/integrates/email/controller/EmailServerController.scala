package com.service.integrates.email.controller

import com.baomidou.mybatisplus.mapper.{EntityWrapper, Wrapper}
import com.service.framework.web.controller.AjaxResult
import com.service.integrates.easypoi.traits.ServiceEasypoiTrait
import com.service.integrates.email.entity.EmailServer
import com.service.integrates.email.service.IEmailServerService
import com.service.integrates.svnkit.admin.controller.BaseController
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation._
import org.springframework.web.multipart.MultipartFile

import scala.collection.JavaConversions._

@Controller
@RequestMapping(Array("/service/email/server"))
class EmailServerController extends BaseController with ServiceEasypoiTrait {
  private val emailServerPrefix = "email/server"
  private val excelTitle = "电子邮件服务器列表"

  @RequiresPermissions(Array("email:server"))
  @GetMapping()
  def index() = emailServerPrefix + "/list"

  @GetMapping(value = Array("/selectEmailServer"))
  def selectEmailServer() = emailServerPrefix + "/selectlist"

  /**
    * 列表
    */
  @RequiresPermissions(Array("email:server:list"))
  @RequestMapping(value = Array("/list"))
  @ResponseBody
  def list(record: EmailServer): Any = {
    val wrapper: Wrapper[EmailServer] = new EntityWrapper[EmailServer]().like("server_name", record.serverName).like("email_host", record.emailHost)
    queryByPage[EmailServer](emailServerService, wrapper)
  }

  /**
    * 数据导入模板
    */
  @RequiresPermissions(Array("email:server:import"))
  @RequestMapping(value = Array("/importTemplate"))
  @ResponseBody
  def importTemplate(): AjaxResult = {
    executeRequest responseAjaxResult {
      generateTemplate(classOf[EmailServer], excelTitle)
    }
  }

  /**
    * 数据导入
    */
  @RequiresPermissions(Array("email:server:import"))
  @RequestMapping(value = Array("/import"))
  @ResponseBody
  def importRepository(file: MultipartFile, updateSupport: Boolean): AjaxResult = {
    executeRequest responseAjax {
      val records = importRecords(file, classOf[EmailServer])
      emailServerService.insertBatch(records)
    }
  }

  /**
    * 新增
    */
  @RequiresPermissions(Array("email:server:add"))
  @GetMapping(Array("/add"))
  def add(): String = {
    emailServerPrefix + "/add"
  }

  /**
    * 新增保存
    */
  @RequiresPermissions(Array("email:server:add"))
  @PostMapping(Array("/add"))
  @ResponseBody
  def addSave(record: EmailServer): AjaxResult = {
    executeRequest responseAjax {
      emailServerService.insert(record)
    }
  }

  /**
    * 数据导出
    */
  @RequiresPermissions(Array("email:server:export"))
  @RequestMapping(value = Array("/export"))
  @ResponseBody
  def export(): AjaxResult = {
    executeRequest responseAjaxResult {
      exportRecords(emailServerService, new EntityWrapper[EmailServer](), classOf[EmailServer], excelTitle)
    }
  }

  /**
    * 修改
    */
  @RequiresPermissions(Array("email:server:edit"))
  @RequestMapping(Array("/edit/{id}"))
  def edit(@PathVariable("id") id: String, mm: ModelMap): String = {
    mm.put("server", emailServerService.selectById(id))
    emailServerPrefix + "/edit"
  }

  /**
    * 修改保存
    */
  @RequiresPermissions(Array("email:server:edit"))
  @PostMapping(Array("/edit"))
  @ResponseBody
  def editSave(record: EmailServer): AjaxResult = {
    executeRequest responseAjax {
      emailServerService.updateById(record)
    }
  }

  /**
    * 删除
    */
  @RequiresPermissions(Array("email:server:delete"))
  @PostMapping(Array("/delete"))
  @ResponseBody
  def delete(ids: String): AjaxResult = {
    executeRequest responseAjax {
      emailServerService.deleteBatchIds(ids.split(",", -1).toList)
    }
  }
}
