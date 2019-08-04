package com.service.framework.web.controller

import java.util

/**
  * @author 伍鲜
  */
class AjaxResult extends util.HashMap[String, Any] {

}

/**
  * @author 伍鲜
  */
object AjaxResult {
  def response(code: String, msg: String): AjaxResult = {
    val result = new AjaxResult
    result.put("code", code)
    result.put("message", msg)
    result
  }

  def success(code: Int, msg: String): AjaxResult = {
    val result = new AjaxResult
    result.put("code", code)
    result.put("message", msg)
    result
  }

  def success(msg: String): AjaxResult = success(200, msg)

  def success(): AjaxResult = success(200, "请求成功")

  def failure(code: Int, msg: String): AjaxResult = {
    val result = new AjaxResult
    result.put("code", code)
    result.put("message", msg)
    result
  }

  def failure(msg: String): AjaxResult = success(500, msg)

  def failure(): AjaxResult = success(500, "请求失败")
}
