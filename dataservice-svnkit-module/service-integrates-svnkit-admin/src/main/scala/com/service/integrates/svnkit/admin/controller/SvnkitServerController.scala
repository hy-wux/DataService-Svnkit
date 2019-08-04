package com.service.integrates.svnkit.admin.controller

import com.baomidou.mybatisplus.mapper.{EntityWrapper, Wrapper}
import com.baomidou.mybatisplus.plugins.Page
import com.service.framework.core.utils.CommUtil
import com.service.framework.web.controller.AjaxResult
import com.service.framework.web.page.PageDomain
import com.service.integrates.easypoi.traits.ServiceEasypoiTrait
import com.service.integrates.email.entity.EmailAccount
import com.service.integrates.svnkit.admin.entity.{SvnkitServer, SvnkitUser}
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation._
import org.springframework.web.multipart.MultipartFile

import scala.collection.JavaConversions._

@Controller
@RequestMapping(Array("/service/svnkit/servers"))
class SvnkitServerController extends BaseController with ServiceEasypoiTrait {
  private val svnkitServerPrefix = "svnkit/server"
  private val excelTitle = "SVN服务器列表"

  @RequiresPermissions(Array("email:server"))
  @GetMapping()
  def index() = svnkitServerPrefix + "/list"

  @GetMapping(value = Array("/selectSvnkitServer"))
  def selectSvnkitServer() = svnkitServerPrefix + "/selectlist"

  /**
    * 列表
    */
  @RequiresPermissions(Array("email:server:list"))
  @RequestMapping(value = Array("/list"))
  @ResponseBody
  def list(record: SvnkitServer): Any = {
    val wrapper: Wrapper[SvnkitServer] = new EntityWrapper[SvnkitServer]().like("server_name", record.serverName).like("server_host", record.serviceName)
    val domain = PageDomain.buildPageDomain()
    val page = svnkitServerService.selectPage(new Page[SvnkitServer](domain.current, domain.size, domain.orderByField, domain.isAsc), wrapper)
    page.setTotal(svnkitServerService.selectCount(wrapper))

    if (page.getRecords.size() > 0) {
      // 邮件信息
      val emailList = page.getRecords.filter(x => CommUtil.isNotEmpty(x.sendEmail)).map(_.sendEmail)
      if (emailList.size > 0) {
        val emailAccounts = emailAccountService.selectBatchIds(emailList)
        page.getRecords.filter(x => CommUtil.isNotEmpty(x.sendEmail)).foreach(server => {
          server.sendEmailDesc = emailAccounts.filter(account => server.sendEmail == account.rowKey)
            .map(account => s"${Option(account.accountName).getOrElse("")}(${Option(account.username).getOrElse("")})")
            .mkString(",")
        })
      }

      // SVN账号信息
      val svnList = page.getRecords.filter(x => CommUtil.isNotEmpty(x.adminAccount)).map(_.adminAccount)
      if (svnList.size > 0) {
        val svnAccounts = userService.selectBatchIds(svnList)
        page.getRecords.filter(x => CommUtil.isNotEmpty(x.adminAccount)).foreach(server => {
          server.adminAccountDesc = svnAccounts.filter(account => server.adminAccount == account.rowKey)
            .map(account => s"${Option(account.staffName).getOrElse("")}(${Option(account.username).getOrElse("")})")
            .mkString(",")
        })
      }
    }

    buildPageData(page)
  }

  /**
    * 数据导入模板
    */
  @RequiresPermissions(Array("email:server:import"))
  @RequestMapping(value = Array("/importTemplate"))
  @ResponseBody
  def importTemplate(): AjaxResult = {
    executeRequest responseAjaxResult {
      generateTemplate(classOf[SvnkitServer], excelTitle)
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
      val records = importRecords(file, classOf[SvnkitServer])
      svnkitServerService.insertBatch(records)
    }
  }

  /**
    * 新增
    */
  @RequiresPermissions(Array("email:server:add"))
  @GetMapping(Array("/add"))
  def add(): String = {
    svnkitServerPrefix + "/add"
  }

  /**
    * 新增保存
    */
  @RequiresPermissions(Array("email:server:add"))
  @PostMapping(Array("/add"))
  @ResponseBody
  def addSave(record: SvnkitServer): AjaxResult = {
    executeRequest responseAjax {
      svnkitServerService.insert(record)
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
      exportRecords(svnkitServerService, new EntityWrapper[SvnkitServer](), classOf[SvnkitServer], excelTitle)
    }
  }

  /**
    * 修改
    */
  @RequiresPermissions(Array("email:server:edit"))
  @RequestMapping(Array("/edit/{id}"))
  def edit(@PathVariable("id") id: String, mm: ModelMap): String = {
    val record = svnkitServerService.selectById(id)
    if (CommUtil.isNotEmpty(record.sendEmail)) {
      Option(emailAccountService.selectById(record.sendEmail)) match {
        case Some(emailAccount) =>
          record.sendEmailDesc = s"${Option(emailAccount.accountName).getOrElse("")}(${Option(emailAccount.username).getOrElse("")})"
        case None =>
      }

      Option(userService.selectById(record.adminAccount)) match {
        case Some(svnAccount) =>
          record.adminAccountDesc = s"${Option(svnAccount.staffName).getOrElse("")}(${Option(svnAccount.username).getOrElse("")})"
        case None =>
      }
    }
    mm.put("server", record)
    svnkitServerPrefix + "/edit"
  }

  /**
    * 修改保存
    */
  @RequiresPermissions(Array("email:server:edit"))
  @PostMapping(Array("/edit"))
  @ResponseBody
  def editSave(record: SvnkitServer): AjaxResult = {
    executeRequest responseAjax {
      svnkitServerService.updateById(record)
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
      svnkitServerService.deleteBatchIds(ids.split(",", -1).toList)
    }
  }
}
