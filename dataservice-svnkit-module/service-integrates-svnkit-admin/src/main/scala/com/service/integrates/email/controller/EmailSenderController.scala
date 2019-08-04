package com.service.integrates.email.controller

import com.service.framework.web.controller.AjaxResult
import com.service.integrates.email.component.ServiceEmailComponent
import com.service.integrates.email.entity.EmailSender
import com.service.integrates.svnkit.admin.controller.BaseController
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._

@Controller
@RequestMapping(Array("/service/email/sender"))
class EmailSenderController extends BaseController {
  private val emailSenderPrefix = "email/sender"

  @RequiresPermissions(Array("email:sender"))
  @GetMapping()
  def index() = emailSenderPrefix + "/sender"

  /**
    * 新增保存
    */
  @RequiresPermissions(Array("email:sender"))
  @PostMapping(Array("/send"))
  @ResponseBody
  def send(record: EmailSender): AjaxResult = {
    executeRequest responseAjax {
      serviceEmailComponent.getEmailSender(record.accountKey).sendEmail(record.emailTo.split(";", -1), null, null, record.subject, record.emailContent)
    }
  }

}
