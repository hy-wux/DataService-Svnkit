package com.service.visual.web.admin.core.aop

import com.service.framework.core.logs.Logging
import com.service.visual.web.admin.exception.InvalidKaptchaException
import org.apache.shiro.authc.{AuthenticationException, CredentialsException, DisabledAccountException}
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.{ControllerAdvice, ExceptionHandler, ResponseStatus}

@ControllerAdvice
@Order(-1)
class GlobalExceptionHandler extends Logging {
  /**
    * 用户未登录异常
    */
  @ExceptionHandler(Array(classOf[AuthenticationException]))
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  def unAuth(e: AuthenticationException, model: Model): String = {
    "/login.html"
  }

  /**
    * 账号被冻结异常
    */
  @ExceptionHandler(Array(classOf[DisabledAccountException]))
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  def accountLocked(e: DisabledAccountException, model: Model): String = {
    model.addAttribute("tips", "账号被冻结")
    "/login.html"
  }

  /**
    * 账号密码错误异常
    */
  @ExceptionHandler(Array(classOf[CredentialsException]))
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  def credentials(e: CredentialsException, model: Model): String = {
    model.addAttribute("tips", "账号密码错误")
    "/login.html"
  }

  /**
    * 验证码错误异常
    */
  @ExceptionHandler(Array(classOf[InvalidKaptchaException]))
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  def credentials(e: InvalidKaptchaException, model: Model): String = {
    model.addAttribute("tips", "验证码错误")
    "/login.html"
  }
}
