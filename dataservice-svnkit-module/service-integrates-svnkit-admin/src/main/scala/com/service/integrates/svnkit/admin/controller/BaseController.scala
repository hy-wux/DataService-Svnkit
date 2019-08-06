package com.service.integrates.svnkit.admin.controller

import com.baomidou.mybatisplus.mapper.{EntityWrapper, Wrapper}
import com.baomidou.mybatisplus.plugins.Page
import com.baomidou.mybatisplus.service.IService
import com.service.framework.core.serializer.ServiceSerializer
import com.service.framework.web.page.PageDomain
import com.service.integrates.email.component.ServiceEmailComponent
import com.service.integrates.email.service.{IEmailAccountService, IEmailServerService}
import com.service.integrates.svnkit.admin.service._
import com.service.integrates.svnkit.api.remote.SvnkitRemoteService
import org.springframework.beans.factory.annotation.{Autowired, Qualifier}
import org.springframework.context.annotation.Lazy
import org.springframework.core.io.Resource
import org.thymeleaf.TemplateEngine

/**
  * @author 伍鲜
  */
class BaseController extends com.service.framework.web.controller.BaseController {

  @Autowired val svnkitRemoteService: SvnkitRemoteService = null

  /* 数据库服务 */

  /**
    * 服务器服务
    */
  @Autowired val svnkitServerService: ISvnkitServerService = null

  /**
    * 仓库服务
    */
  @Autowired val repositoryService: ISvnkitRepositoryService = null

  /**
    * 组别服务
    */
  @Autowired val groupService: ISvnkitGroupService = null

  /**
    * 用户服务
    */
  @Autowired val userService: ISvnkitUserService = null

  /* 邮件服务 */

  @Autowired val emailAccountService: IEmailAccountService = null
  @Autowired val emailServerService: IEmailServerService = null

  /**
    * 集成Mail服务
    */
  @Autowired val serviceEmailComponent: ServiceEmailComponent = null

  @Qualifier("svnkitEmailInlines")
  @Autowired val emailConfig: Map[String, Resource] = null

  @Autowired val templateEngine: TemplateEngine = null

  /* 其他组件 */

  @Autowired val serviceSerializer: ServiceSerializer = null

  @Lazy
  @Autowired val onlineUserSession: OnlineUserSession = null

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
