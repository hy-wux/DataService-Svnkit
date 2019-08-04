package com.service.integrates.email.controller

import com.baomidou.mybatisplus.mapper.{EntityWrapper, Wrapper}
import com.baomidou.mybatisplus.plugins.Page
import com.service.framework.core.utils.CommUtil
import com.service.framework.web.controller.AjaxResult
import com.service.framework.web.page.PageDomain
import com.service.integrates.easypoi.traits.ServiceEasypoiTrait
import com.service.integrates.email.entity.{EmailAccount, EmailServer}
import com.service.integrates.svnkit.admin.controller.BaseController
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation._

import scala.collection.JavaConversions._

@Controller
@RequestMapping(Array("/service/email/account"))
class EmailAccountController extends BaseController with ServiceEasypoiTrait {
  private val accountPrefix = "email/account"

  @RequiresPermissions(Array("email:account"))
  @GetMapping()
  def index() = accountPrefix + "/list"

  @GetMapping(value = Array("/selectEmailAccount"))
  def selectEmailAccount() = accountPrefix + "/selectlist"

  /**
    * 列表
    */
  @RequiresPermissions(Array("email:account:list"))
  @RequestMapping(value = Array("/list"))
  @ResponseBody
  def list(record: EmailAccount): Any = {
    val wrapper: Wrapper[EmailAccount] = if (CommUtil.isNotEmpty(record.serverKey)) {
      new EntityWrapper[EmailAccount]().eq("server_key", record.serverKey).like("account_name", record.accountName).like("username", record.username)
    } else {
      new EntityWrapper[EmailAccount].like("account_name", record.accountName).like("username", record.username)
    }
    val domain = PageDomain.buildPageDomain()
    val page = emailAccountService.selectPage(new Page[EmailAccount](domain.current, domain.size, domain.orderByField, domain.isAsc), wrapper)
    page.setTotal(emailAccountService.selectCount(wrapper))

    if (page.getRecords.size() > 0) {
      val servers = emailServerService.selectBatchIds(page.getRecords.filter(x => CommUtil.isNotEmpty(x.serverKey)).map(_.serverKey))
      page.getRecords.filter(x => CommUtil.isNotEmpty(x.serverKey)).foreach(account => {
        account.serverName = servers.filter(param => account.serverKey == param.rowKey).map(param => Option(param.serverName).getOrElse("")).mkString(",")
      })
    }

    buildPageData(page)
  }

  /**
    * 新增
    */
  @RequiresPermissions(Array("email:account:add"))
  @GetMapping(Array("/add"))
  def add: String = accountPrefix + "/add"

  /**
    * 校验
    */
  @PostMapping(Array("/checkRecordUnique"))
  @ResponseBody
  def checkRecordUnique(record: EmailAccount): String = {
    var result: String = null
    if (CommUtil.isNotNull(record) && CommUtil.isNotEmpty(record.rowKey)) {
      result = s"${emailAccountService.selectCount(new EntityWrapper[EmailAccount]().ne("row_key", record.rowKey).eq("server_key", record.serverKey).eq("username", record.username))}"
    } else if (CommUtil.isNotNull(record)) {
      result = s"${emailAccountService.selectCount(new EntityWrapper[EmailAccount]().eq("server_key", record.serverKey).eq("username", record.username))}"
    } else {
      result = "0"
    }
    result
  }

  /**
    * 新增保存
    */
  @RequiresPermissions(Array("email:account:add"))
  @PostMapping(Array("/add"))
  @ResponseBody
  def addSave(record: EmailAccount): AjaxResult = {
    executeRequest responseAjax {
      record.rowKey = s"${idWorker.nextId()}"
      emailAccountService.insert(record)
      serviceEmailComponent.initEmailSender(record.rowKey)
    }
  }

  /**
    * 修改
    */
  @RequiresPermissions(Array("email:account:edit"))
  @RequestMapping(Array("/edit/{id}"))
  def edit(@PathVariable("id") id: String, mm: ModelMap): String = {
    val record = emailAccountService.selectById(id)
    if (CommUtil.isNotEmpty(record.serverKey)) {
      record.serverName = Option(emailServerService.selectById(record.serverKey)).getOrElse(new EmailServer).serverName
    }

    mm.put("account", record)
    accountPrefix + "/edit"
  }

  /**
    * 修改保存
    */
  @RequiresPermissions(Array("email:account:edit"))
  @PostMapping(Array("/edit"))
  @ResponseBody
  def editSave(record: EmailAccount): AjaxResult = {
    executeRequest responseAjax {
      emailAccountService.updateById(record)
      serviceEmailComponent.initEmailSender(record.rowKey)
    }
  }

  /**
    * 删除
    */
  @RequiresPermissions(Array("email:account:delete"))
  @PostMapping(Array("/delete"))
  @ResponseBody
  def delete(ids: String): AjaxResult = {
    executeRequest responseAjax {
      val list = ids.split(",", -1).toList
      emailAccountService.deleteBatchIds(list)
      serviceEmailComponent.removeEmailSender(list)
    }
  }
}
