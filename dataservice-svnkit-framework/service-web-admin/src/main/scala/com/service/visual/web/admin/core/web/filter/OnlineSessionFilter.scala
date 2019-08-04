package com.service.visual.web.admin.core.web.filter

import com.service.framework.core.utils.CommUtil
import javax.servlet.{ServletRequest, ServletResponse}
import org.apache.shiro.web.filter.AccessControlFilter

class OnlineSessionFilter extends AccessControlFilter {
  override def isAccessAllowed(request: ServletRequest, response: ServletResponse, o: scala.Any): Boolean = {
    val subject = getSubject(request, response)
    if (CommUtil.isEmpty(subject) || CommUtil.isEmpty(subject.getSession)) {
      true
    } else {
      false
    }
  }

  override def onAccessDenied(request: ServletRequest, response: ServletResponse): Boolean = true
}
