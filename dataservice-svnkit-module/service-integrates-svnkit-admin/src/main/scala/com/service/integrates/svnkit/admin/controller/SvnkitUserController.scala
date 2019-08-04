package com.service.integrates.svnkit.admin.controller

import java.util

import com.baomidou.mybatisplus.mapper.{EntityWrapper, Wrapper}
import com.service.framework.core.encrypt.MD5
import com.service.framework.core.utils.{CommUtil, DateUtil, EncryptUtil}
import com.service.framework.web.controller.{AjaxResult, ControllerInitBinder}
import com.service.integrates.easypoi.traits.ServiceEasypoiTrait
import com.service.integrates.email.entity.EmailAccount
import com.service.integrates.svnkit.admin.entity.{SvnkitServer, SvnkitUser}
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation._
import org.springframework.web.multipart.MultipartFile
import org.thymeleaf.context.Context

import scala.collection.JavaConversions._

@Controller
@RequestMapping(Array("/service/svnkit/users"))
class SvnkitUserController extends BaseController with ServiceEasypoiTrait with ControllerInitBinder {
  private val PREFIX = "svnkit/users"
  private val excelTitle = "SVN用户列表"

  @RequiresPermissions(Array("svnkit:users:manage"))
  @GetMapping()
  def userIndex() = PREFIX + "/list"

  /**
    * 分页列表
    */
  @RequiresPermissions(Array("svnkit:users:list"))
  @RequestMapping(value = Array("/list"))
  @ResponseBody
  def list(record: SvnkitUser): Any = {
    val wrapper: Wrapper[SvnkitUser] = new EntityWrapper[SvnkitUser]()
      .like("server_key", record.serverKey)
      .like("username", record.username)
      .like("staff_name", record.staffName)
      .like("staff_num", record.staffNum)
      .like("staff_email", record.staffEmail)
    queryByPage[SvnkitUser](userService, wrapper)
  }

  /**
    * 初始化数据库
    */
  @RequiresPermissions(Array("svnkit:users:init"))
  @PostMapping(Array("/init"))
  @ResponseBody
  def init(serverKey: String): AjaxResult = {
    executeRequest responseAjax {
      if (CommUtil.isEmpty(serverKey)) {
        throw new Exception("请指定服务器")
      }
      val server = svnkitServerService.selectById(serverKey)
      val userExists = svnkitRemoteService.listUsers(server.serviceName)
      val userLists = userService.selectList(new EntityWrapper[SvnkitUser]().eq("server_key", serverKey))
      // 添加存在的用户
      userExists.filter(x => userLists.filter(_.username == x).size == 0).foreach(x => {
        val user = new SvnkitUser
        user.username = x
        user.serverKey = serverKey
        user.updateDatetime = DateUtil.getCurrentDate
        userService.insert(user)
      })
      // 删除不存在的用户
      userLists.filter(user => !userExists.contains(user.username)).foreach(user => {
        userService.deleteById(user.rowKey)
      })
    }
  }

  /**
    * 数据导入模板
    */
  @RequiresPermissions(Array("svnkit:users:import"))
  @RequestMapping(value = Array("/importTemplate"))
  @ResponseBody
  def importTemplate(): AjaxResult = {
    executeRequest responseAjaxResult {
      generateTemplate(classOf[SvnkitUser], excelTitle)
    }
  }

  /**
    * 数据导入<br/>
    *
    * 数据导入流程：<br/>
    * 1、完成初始化操作，目的是实现服务器数据与数据库数据的同步<br/>
    * 2、获取上传的Excel文件中的数据，并转换成实体<br/>
    * 3、根据实体创建用户<br/>
    * 4、将数据保存到数据库，需要判断是否支持更新，如果支持更新则插入或更新，否则筛选出新建的用户进行插入<br/>
    * 5、对新创建的用户发送邮件通知<br/>
    */
  @RequiresPermissions(Array("svnkit:users:import"))
  @RequestMapping(value = Array("/import/{serverKey}"))
  @ResponseBody
  def importUser(file: MultipartFile, updateSupport: Boolean, @PathVariable("serverKey") serverKey: String): AjaxResult = {
    executeRequest responseAjax {
      // 1、完成初始化操作，目的是实现服务器数据与数据库数据的同步
      init(serverKey)
      val server = svnkitServerService.selectById(serverKey)
      val emailAccount = emailAccountService.selectById(server.sendEmail)

      // 2、获取上传的Excel文件中的数据，并转换成实体
      val users = importRecords(file, classOf[SvnkitUser])

      if (users.length < 1) {
        throw new Exception("导入的数据为空")
      }
      if (users.groupBy(_.username).filter(_._2.size > 1).size > 0) {
        throw new Exception("导入的数据中存在相同用户名称的数据")
      }
      // 查询出现有的用户
      val existsUsers: util.List[SvnkitUser] = userService.selectList(new EntityWrapper[SvnkitUser]().eq("server_key", serverKey).in("username", users.map(_.username)))

      // 找出创建的用户
      val createUsers: util.List[SvnkitUser] = users.filter(user => {
        existsUsers.filter(exists => {
          exists.username == user.username
        }).size == 0
      }).map(user => {
        user.password = MD5.getMD5(user.username).toUpperCase
        user.serverKey = serverKey
        user
      })

      val createSize = createUsers.size()

      if (createSize > 0) {
        // 3、根据实体创建用户
        info("开始创建用户")
        svnkitRemoteService.createUserBatch(server.serviceName, createUsers.map(user => (user.username, user.password)).toMap[String, String])

        // 4、将数据保存到数据库
        info("保存到数据库")

        // 插入新增数据
        userService.insertBatch(createUsers.map(user => {
          user.password = EncryptUtil.encrypt3DES(user.password)
          user
        }))

        // 5、对新创建的用户发送邮件通知
        if (CommUtil.isNotEmpty(emailAccount)) {
          createUsers.filter(user => CommUtil.isNotEmpty(user.staffEmail)).map(user => {
            user.password = EncryptUtil.decrypt3DES(user.password)
            user
          }).foreach(user => sendUserCreateEmail(server, emailAccount, user))
        }
      }
      // 需要判断是否支持更新
      if (updateSupport) {
        // 更新已有数据
        existsUsers.filter(exists => {
          users.filter(user => {
            exists.username == user.username && exists.serverKey == serverKey
          }).size > 0
        }).foreach(user => {
          users.filter(_.username == user.username)
            .foreach(importUser => {
              user.staffName = importUser.staffName
              user.staffEmail = importUser.staffEmail
              user.staffNum = importUser.staffNum
              userService.updateById(user)
            })
        })
      }
    }
  }

  /**
    * 新增界面
    */
  @RequiresPermissions(Array("svnkit:users:add"))
  @GetMapping(Array("/add"))
  def add(): String = {
    PREFIX + "/add"
  }

  /**
    * 新增保存
    */
  @RequiresPermissions(Array("svnkit:users:add"))
  @PostMapping(Array("/add"))
  @ResponseBody
  def addSave(record: SvnkitUser): AjaxResult = {
    executeRequest responseAjax {
      // 设置SVN用户密码
      record.password = MD5.getMD5(record.username).toUpperCase

      val server = svnkitServerService.selectById(record.serverKey)
      val emailAccount = emailAccountService.selectById(server.sendEmail)

      // 创建用户
      svnkitRemoteService.createUser(server.serviceName, record.username, record.password)

      // 保存到数据库
      record.password = EncryptUtil.encrypt3DES(record.password)
      userService.insert(record)

      if (CommUtil.isNotEmpty(record.staffEmail) && CommUtil.isNotEmpty(emailAccount)) {
        // 发送邮件
        record.password = EncryptUtil.decrypt3DES(record.password)
        sendUserCreateEmail(server, emailAccount, record)
      }
    }
  }

  /**
    * 数据导出
    */
  @RequiresPermissions(Array("svnkit:users:export"))
  @RequestMapping(value = Array("/export"))
  @ResponseBody
  def export(record: SvnkitUser): AjaxResult = {
    executeRequest responseAjaxResult {
      val wrapper: Wrapper[SvnkitUser] = new EntityWrapper[SvnkitUser]()
        .like("server_key", record.serverKey)
        .like("username", record.username)
        .like("staff_name", record.staffName)
        .like("staff_num", record.staffNum)
        .like("staff_email", record.staffEmail)
      exportRecords(userService, wrapper, classOf[SvnkitUser], excelTitle)
    }
  }

  /**
    * 编辑页面
    */
  @RequiresPermissions(Array("svnkit:users:edit"))
  @GetMapping(Array("/edit/{id}"))
  def edit(@PathVariable("id") id: String, mm: ModelMap): String = {
    mm.put("user", userService.selectById(id))
    PREFIX + "/edit"
  }

  /**
    * 编辑保存
    */
  @RequiresPermissions(Array("svnkit:users:edit"))
  @PostMapping(Array("/edit"))
  @ResponseBody
  def editSave(record: SvnkitUser): AjaxResult = {
    executeRequest responseAjax {
      // 加密密码
      if (CommUtil.isNotEmpty(record.password)) {

        val server = svnkitServerService.selectById(record.serverKey)
        val emailAccount = emailAccountService.selectById(server.sendEmail)

        // 更改用户密码
        svnkitRemoteService.updateUser(server.serviceName, record.username, record.password)

        // 保存到数据库
        record.password = EncryptUtil.encrypt3DES(record.password)
        userService.updateById(record)

        if (CommUtil.isNotEmpty(record.staffEmail) && CommUtil.isNotEmpty(emailAccount)) {
          // 发送邮件
          record.password = EncryptUtil.decrypt3DES(record.password)
          sendUserPasswordEmail(server, emailAccount, record)
        }
      } else {
        userService.updateById(record)
      }
    }
  }

  /**
    * 查看页面
    */
  @RequiresPermissions(Array("svnkit:users:view"))
  @GetMapping(Array("/view/{id}"))
  def view(@PathVariable("id") id: String, mm: ModelMap): String = {
    val user = userService.selectById(id)
    // 密码解密
    user.password = EncryptUtil.decrypt3DES(user.password)
    mm.put("user", user)
    PREFIX + "/view"
  }

  @RequiresPermissions(Array("svnkit:users:self"))
  @GetMapping(value = Array("/self"))
  def selfIndex() = PREFIX + "/self"

  /**
    * 查询自己的用户
    */
  @RequiresPermissions(Array("svnkit:users:self"))
  @RequestMapping(value = Array("/selfList"))
  @ResponseBody
  def selfList(record: SvnkitUser): Any = {
    val wrapper: Wrapper[SvnkitUser] = new EntityWrapper[SvnkitUser]()
      .eq("login_account", onlineUserSession.getOnlineUserAccount)
      .like("server_key", record.serverKey)
      .like("username", record.username)
      .like("staff_name", record.staffName)
      .like("staff_num", record.staffNum)
      .like("staff_email", record.staffEmail)
    queryByPage[SvnkitUser](userService, wrapper)
  }

  /* 邮件服务 */

  /**
    * 创建用户发送邮件
    *
    * @param user
    */
  def sendUserCreateEmail(server: SvnkitServer, email: EmailAccount, user: SvnkitUser): Unit = {
    // 邮件内容渲染
    val context = new Context()
    context.setVariable("contentTemplate", "svnkit/email/UserCreate")

    context.setVariable("Username", user.staffName)
    context.setVariable("SVNUsername", user.username)
    context.setVariable("SVNPassword", user.password)

    context.setVariable("SVNRepositoryRoot", if (server.serverAddress.endsWith("/")) server.serverAddress.substring(0, server.serverAddress.length - 1) else server.serverAddress)
    context.setVariable("AdminEmail", email.username)
    context.setVariable("AdminName", server.adminName)
    context.setVariable("SENDDatetime", DateUtil.formatShort(DateUtil.getCurrentDate))

    val content = templateEngine.process("svnkit/email/SvnkitEmailTemplate", context)

    // 发送邮件
    serviceEmailComponent.getEmailSender(email.rowKey).sendEmailByResource(Array(user.staffEmail), null, null, "SVN用户创建成功", null, emailConfig, content)
  }

  /**
    * 重置密码发送邮件
    *
    * @param user
    */
  def sendUserPasswordEmail(server: SvnkitServer, email: EmailAccount, user: SvnkitUser): Unit = {
    // 邮件内容渲染
    val context = new Context()
    context.setVariable("contentTemplate", "svnkit/email/UserPassword")

    context.setVariable("Username", user.staffName)
    context.setVariable("SVNUsername", user.username)
    context.setVariable("SVNPassword", user.password)

    context.setVariable("AdminEmail", email.username)
    context.setVariable("AdminName", server.adminName)
    context.setVariable("SENDDatetime", DateUtil.formatShort(DateUtil.getCurrentDate))

    val content = templateEngine.process("svnkit/email/SvnkitEmailTemplate", context)

    // 发送邮件
    serviceEmailComponent.getEmailSender(email.rowKey).sendEmailByResource(Array(user.staffEmail), null, null, "SVN密码修改成功", null, emailConfig, content)
  }
}
