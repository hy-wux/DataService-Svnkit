package com.service.framework.web.page

import com.service.framework.core.utils.StringUtil
import com.service.framework.web.utils.ServletUtils

import scala.beans.BeanProperty

class PageDomain {
  /** 当前记录起始索引 */
  @BeanProperty var current: Int = _
  /** 每页显示记录数 */
  @BeanProperty var size: Int = _
  /** 排序列 */
  @BeanProperty var orderByField: String = _
  /** 排序的类型 */
  @BeanProperty var isAsc: Boolean = _
}

object PageDomain {
  def buildPageDomain(): PageDomain = {
    val pageDomain = new PageDomain
    pageDomain.setCurrent(ServletUtils.getParameterToInt("current"))
    pageDomain.setSize(ServletUtils.getParameterToInt("size"))
    pageDomain.setOrderByField(StringUtil.toUnderScoreCase(ServletUtils.getParameter( "orderField")))
    pageDomain.setIsAsc(ServletUtils.getParameter("orderType").equalsIgnoreCase("asc"))
    pageDomain
  }
}