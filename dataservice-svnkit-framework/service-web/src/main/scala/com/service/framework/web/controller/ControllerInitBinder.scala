package com.service.framework.web.controller

import java.text.SimpleDateFormat
import java.util.Date

import org.springframework.beans.propertyeditors.CustomDateEditor
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.InitBinder

trait ControllerInitBinder {
  /**
    * 将前台传递过来的日期格式的字符串，自动转化为Date类型
    */
  @InitBinder
  def initBinder(binder: WebDataBinder): Unit = {
    val dateFormat = new SimpleDateFormat("yyyy-MM-dd")
    dateFormat.setLenient(false)
    binder.registerCustomEditor(classOf[Date], new CustomDateEditor(dateFormat, true))
  }
}
