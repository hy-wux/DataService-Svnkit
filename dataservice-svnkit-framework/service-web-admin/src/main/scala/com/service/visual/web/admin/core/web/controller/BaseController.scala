package com.service.visual.web.admin.core.web.controller

import com.baomidou.mybatisplus.mapper.{EntityWrapper, Wrapper}
import com.baomidou.mybatisplus.plugins.Page
import com.baomidou.mybatisplus.service.IService
import com.service.framework.core.PubFunction
import com.service.framework.core.logs.Logging
import com.service.framework.web.page.PageDomain

/**
  * @author 伍鲜
  */
class BaseController extends com.service.framework.web.controller.BaseController with PubFunction with Logging {

  /**
    * 分页查询
    *
    * @param service
    * @param params
    * @tparam T
    * @return
    */
  def basePageQuery[T](service: IService[T], params: List[(String, String, String)]): Any = {
    val wrapper: Wrapper[T] = new EntityWrapper[T]()
    params.foreach(param => {
      param._2.toLowerCase match {
        case "like" => wrapper.like(param._1, param._3)
        case "eq" => wrapper.eq(param._1, param._3)
      }
    })
    val domain = PageDomain.buildPageDomain()
    val page = service.selectPage(new Page[T](domain.current, domain.size, domain.orderByField, domain.isAsc), wrapper)
    page.setTotal(service.selectCount(wrapper))
    buildPageData(page)
  }

  /**
    * 分页查询
    *
    * @param service
    * @param wrapper
    * @tparam T
    * @return
    */
  def basePageQuery[T](service: IService[T], wrapper: Wrapper[T]): Any = {
    val domain = PageDomain.buildPageDomain()
    val page = service.selectPage(new Page[T](domain.current, domain.size, domain.orderByField, domain.isAsc), wrapper)
    page.setTotal(service.selectCount(wrapper))
    buildPageData(page)
  }

  /**
    * 校验记录是否唯一
    *
    * @param service
    * @param params
    * @tparam T
    * @return
    */
  def baseCheckUnique[T](service: IService[T], params: List[(String, String)]): Boolean = {
    val wrapper: Wrapper[T] = new EntityWrapper[T]()
    params.foreach(param => {
      wrapper.eq(param._1, param._2)
    })
    service.selectCount(wrapper) == 0
  }
}
