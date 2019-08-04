package com.service.framework.web.utils

import com.service.framework.core.utils.StringUtil
import javax.servlet.http.{HttpServletRequest, HttpServletResponse, HttpSession}
import org.springframework.web.context.request.{RequestContextHolder, ServletRequestAttributes}

class ServletUtils {

}

/**
  * @author 伍鲜
  *
  *         Servlet工具类
  */
object ServletUtils {
  /**
    * 获取String参数
    *
    * @param name
    * @return
    */
  def getParameter(name: String): String = getRequest.getParameter(name)

  /**
    * 获取Integer参数
    *
    * @param name
    * @return
    */
  def getParameterToInt(name: String): Integer = getRequest.getParameter(name).toInt

  /**
    * 获取Request
    *
    * @return
    */
  def getRequest: HttpServletRequest = getRequestAttributes.getRequest

  /**
    * 获取Response
    *
    * @return
    */
  def getResponse: HttpServletResponse = getRequestAttributes.getResponse

  /**
    * 获取Session
    *
    * @return
    */
  def getSession: HttpSession = getRequest.getSession

  /**
    * 获取RequestAttributes
    *
    * @return
    */
  def getRequestAttributes: ServletRequestAttributes = RequestContextHolder.getRequestAttributes.asInstanceOf[ServletRequestAttributes]

  /**
    * 将字符串渲染到客户端
    *
    * @param response 渲染对象
    * @param string   待渲染的字符串
    */
  def renderString(response: HttpServletResponse, string: String): String = {
    try {
      response.setContentType("application/json")
      response.setCharacterEncoding("utf-8")
      response.getWriter.print(string)
    } catch {
      case _: Throwable =>
    }
    null
  }

  /**
    * 是否是Ajax异步请求
    *
    * @param request
    */
  def isAjaxRequest(request: HttpServletRequest): Boolean = {
    val accept = request.getHeader("accept")
    if (accept != null && accept.indexOf("application/json") != -1) return true

    val xRequestedWith = request.getHeader("X-Requested-With")
    if (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1) return true

    val uri = request.getRequestURI
    if (StringUtil.inStringIgnoreCase(uri, ".json", ".xml")) return true

    val ajax = request.getParameter("__ajax")
    if (StringUtil.inStringIgnoreCase(ajax, "json", "xml")) return true

    false
  }
}
