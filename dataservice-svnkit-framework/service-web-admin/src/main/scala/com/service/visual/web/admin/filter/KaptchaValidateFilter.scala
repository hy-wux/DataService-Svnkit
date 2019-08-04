package com.service.visual.web.admin.filter

import com.google.code.kaptcha.Constants
import com.service.framework.core.utils.CommUtil
import javax.servlet.http.HttpServletRequest
import javax.servlet.{ServletRequest, ServletResponse}
import org.apache.shiro.web.filter.AccessControlFilter
import org.springframework.beans.factory.annotation.Value

class KaptchaValidateFilter extends AccessControlFilter {
  @Value("${service.visual.web.use-kaptcha:false}")
  val useKaptcha: Boolean = false

  override def isAccessAllowed(request: ServletRequest, response: ServletResponse, o: scala.Any): Boolean = {
    if (!useKaptcha || !("post" == request.asInstanceOf[HttpServletRequest].getMethod.toLowerCase)) {
      true
    } else {
      val kaptcha = request.getParameter("kaptcha")
      val code = request.asInstanceOf[HttpServletRequest].getSession.getAttribute(Constants.KAPTCHA_SESSION_KEY)
      if (CommUtil.isEmpty(kaptcha) || kaptcha != code) {
        false
      } else {
        true
      }
    }
  }

  override def onAccessDenied(request: ServletRequest, response: ServletResponse): Boolean = {
    request.setAttribute("kaptcha", "验证码错误")
    true
  }
}
