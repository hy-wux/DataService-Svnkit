package com.service.visual.web.admin.controller

import java.util

import com.service.framework.core.component.SpringContextHolder
import com.service.framework.core.utils.CommUtil
import com.service.framework.web.controller.AjaxResult
import com.service.framework.web.utils.{ServletUtils, ShiroUtils}
import com.service.visual.web.admin.ServiceWebAdminConfigurationProperties
import com.service.visual.web.admin.core.web.controller.BaseController
import com.service.visual.web.admin.entity.MenuNode
import com.service.visual.web.admin.modules.upms.service.IMenuService
import javax.servlet.http.HttpServletRequest
import org.apache.shiro.authc.{AuthenticationException, UsernamePasswordToken}
import org.apache.shiro.subject.Subject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, ResponseBody}

import scala.collection.JavaConversions._

@Controller
class LoginController extends BaseController {

  @Autowired val menuService: IMenuService = null
  @Autowired val properties: ServiceWebAdminConfigurationProperties = null

  /**
    * 首页访问
    *
    * @param model
    * @param request
    * @return
    */
  @RequestMapping(value = Array("/"), method = Array(RequestMethod.GET))
  def index(model: Model, request: HttpServletRequest): String = {
    request.getSession.setAttribute("kaptcha", properties.getUseKaptcha)
    request.getSession.setAttribute("appname", properties.getApplicationName)
    val roleList = if (ShiroUtils.getUser == null) List[String]() else ShiroUtils.getUser.getRoleList
    val shiroUser = ShiroUtils.getUser
    request.getSession.setAttribute("shiro", shiroUser)

    // 登录时根据角色获取菜单
    val tempList = menuService.queryMenusByLoginRoles(roleList)
    // 获取顶级菜单
    // val topsList = tempList.filter(_.getLevels == 1)
    val topsList = tempList.filter(x => CommUtil.isEmpty(x.getPid))

    val menusList: util.List[MenuNode] = new util.ArrayList[MenuNode]()
    // 添加子菜单树
    // 一级菜单
    topsList.sortWith(_.getSortNumber < _.getSortNumber).foreach(first => {
      // 二级菜单
      first.getChildren.addAll(tempList.filter(_.getPid == first.getId).sortWith(_.getSortNumber < _.getSortNumber).map(second => {
        // 三级菜单
        second.getChildren.addAll(tempList.filter(_.getPid == second.getId).sortWith(_.getSortNumber < _.getSortNumber).map(third => {
          // 四级菜单
          third.getChildren.addAll(tempList.filter(_.getPid == third.getId).sortWith(_.getSortNumber < _.getSortNumber))
          third
        }))
        second
      }))
      menusList.add(first)
    })

    model.addAttribute("menusList", menusList)

    "index"
  }

  /**
    * 登录页面访问
    *
    * @param request
    * @return
    */
  @RequestMapping(value = Array("/login"), method = Array(RequestMethod.GET))
  def login(request: HttpServletRequest): String = {
    request.getSession.setAttribute("kaptcha", properties.getUseKaptcha)
    request.getSession.setAttribute("appname", properties.getApplicationName)
    "login"
  }

  /**
    * 登录表单提交
    *
    * @param username
    * @param password
    * @param remember
    * @return
    */
  @RequestMapping(value = Array("/login"), method = Array(RequestMethod.POST))
  @ResponseBody
  def loginPost(username: String, password: String, remember: String): AjaxResult = {

    val request = ServletUtils.getRequest
    // 验证码校验
    if (CommUtil.isNotEmpty(request.getAttribute("kaptcha"))) {
      failure(request.getAttribute("kaptcha").toString)
    } else {
      val currentUser: Subject = ShiroUtils.getSubject
      val token = new UsernamePasswordToken(username, password, "on" == remember)
      try {
        currentUser.login(token)
        success
      } catch {
        case ex: AuthenticationException => failure("用户或密码错误")
      }
    }
  }

  /**
    * 主页
    *
    * @param request
    * @return
    */
  @RequestMapping(value = Array("/main"), method = Array(RequestMethod.GET))
  def main(request: HttpServletRequest): String = "main"

  /**
    * 退出登录
    */
  @RequestMapping(value = Array("/logout"), method = Array(RequestMethod.GET))
  def logout: String = {
    ShiroUtils.getSubject.logout()
    redirect("/login")
  }
}