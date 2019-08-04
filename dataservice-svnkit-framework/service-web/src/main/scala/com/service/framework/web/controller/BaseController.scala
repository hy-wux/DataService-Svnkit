package com.service.framework.web.controller

import java.util

import com.baomidou.mybatisplus.mapper.Wrapper
import com.baomidou.mybatisplus.plugins.Page
import com.baomidou.mybatisplus.service.IService
import com.service.framework.core.logs.Logging
import com.service.framework.web.page.PageDomain

/**
  * @author 伍鲜
  */
class BaseController extends Logging {

  /**
    * 返回成功
    */
  def success: AjaxResult = AjaxResult.success

  /**
    * 返回失败
    */
  def failure: AjaxResult = AjaxResult.failure

  /**
    * 返回成功
    */
  def success(message: String): AjaxResult = AjaxResult.success(message)

  /**
    * 返回失败
    */
  def failure(message: String): AjaxResult = AjaxResult.failure(message)

  /**
    * 返回成功
    */
  def success(code: Int, message: String): AjaxResult = AjaxResult.success(code, message)

  /**
    * 返回失败
    */
  def failure(code: Int, message: String): AjaxResult = AjaxResult.failure(code, message)

  /**
    * 返回
    */
  def response(code: String, message: String): AjaxResult = AjaxResult.response(code, message)

  /**
    * 页面跳转
    */
  def redirect(url: String): String = s"redirect:${url}"


  /**
    * 构建分页数据
    *
    * @param page
    * @return
    */
  def buildPageData(page: Page[_ <: Any]): util.Map[String, Any] = {
    val map = new util.HashMap[String, Any]()
    map.put("total", page.getTotal)
    map.put("rows", page.getRecords)
    map.put("code", 200)
    map
  }

  /**
    * 根据条件进行分页查询
    *
    * @param service
    * @param wrapper
    * @tparam T
    * @return
    */
  def queryByPage[T](service: IService[T], wrapper: Wrapper[T]): util.Map[String, Any] = {
    val domain = PageDomain.buildPageDomain()
    val page = service.selectPage(new Page[T](domain.current, domain.size, domain.orderByField, domain.isAsc), wrapper)
    page.setTotal(service.selectCount(wrapper))
    buildPageData(page)
  }

  /**
    * 执行Request请求
    *
    * @return
    */
  val executeRequest = new {
    /**
      * 返回默认的Ajax信息
      *
      * @param code
      * @return
      */
    def responseAjax(code: => Any): AjaxResult = {
      try {
        code
        success
      } catch {
        case t: Throwable => {
          t.printStackTrace()
          failure(t.getMessage)
        }
      }
    }

    /**
      * 将执行结果作为Ajax消息返回
      *
      * @param code
      * @return
      */
    def responseAjaxResult(code: => String): AjaxResult = {
      try {
        success(code)
      } catch {
        case t: Throwable => failure(t.getMessage)
      }
    }

    /**
      * 返回自定义消息的Ajax信息
      *
      * @param code
      * @return
      */
    def responseAjaxSpecial(code: => Any, message: String): AjaxResult = {
      try {
        code
        success(message)
      } catch {
        case t: Throwable => failure(t.getMessage)
      }
    }
  }
}
