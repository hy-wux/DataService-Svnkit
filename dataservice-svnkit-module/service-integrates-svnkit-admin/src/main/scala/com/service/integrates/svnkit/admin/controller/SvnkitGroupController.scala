package com.service.integrates.svnkit.admin.controller

import java.util

import com.baomidou.mybatisplus.mapper.EntityWrapper
import com.service.framework.core.utils.{CommUtil, DateUtil}
import com.service.framework.web.controller.{AjaxResult, ControllerInitBinder}
import com.service.integrates.easypoi.traits.ServiceEasypoiTrait
import com.service.integrates.email.entity.EmailAccount
import com.service.integrates.svnkit.admin.entity.{SvnkitGroup, SvnkitRepository, SvnkitServer, SvnkitUser}
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation._
import org.springframework.web.multipart.MultipartFile
import org.thymeleaf.context.Context

import scala.collection.JavaConversions._

@Controller
@RequestMapping(Array("/service/svnkit/groups"))
class SvnkitGroupController extends BaseController with ServiceEasypoiTrait with ControllerInitBinder {
  private val PREFIX = "svnkit/groups"
  private val excelTitle = "SVN组别列表"

  @RequiresPermissions(Array("svnkit:groups:manage"))
  @GetMapping()
  def groupIndex() = PREFIX + "/list"

  /**
    * 分页列表
    */
  @RequiresPermissions(Array("svnkit:groups:list"))
  @RequestMapping(value = Array("/list"))
  @ResponseBody
  def list(record: SvnkitGroup): Any = {
    val wrapper = new EntityWrapper[SvnkitGroup]()
      .like("server_key", record.serverKey)
      .like("group_name", record.groupName)
      .like("group_desc", record.groupDesc)
    queryByPage[SvnkitGroup](groupService, wrapper)
  }

  /**
    * 初始化数据库
    */
  @RequiresPermissions(Array("svnkit:groups:init"))
  @PostMapping(Array("/init"))
  @ResponseBody
  def init(serverKey: String): AjaxResult = {
    executeRequest responseAjax {
      if (CommUtil.isEmpty(serverKey)) {
        throw new Exception("请指定服务器")
      }
      val server = svnkitServerService.selectById(serverKey)
      // 插入组别
      val groupExists = svnkitRemoteService.loadAllGroups(server.serviceName)
      val groupLists = groupService.selectList(new EntityWrapper[SvnkitGroup]().eq("server_key", serverKey))

      // 添加存在的组别
      groupExists.filter(x => groupLists.filter(_.groupName == x._1).size == 0).foreach(x => {
        val group = new SvnkitGroup
        group.groupName = x._1
        group.serverKey = serverKey
        group.updateDatetime = DateUtil.getCurrentDate
        groupService.insert(group)
      })
      // 删除不存在的组别
      groupLists.filter(x => !groupExists.keySet().contains(x.groupName)).foreach(x => {
        groupService.deleteById(x.rowKey)
      })
    }
  }

  /**
    * 数据导入模板
    */
  @RequiresPermissions(Array("svnkit:groups:import"))
  @RequestMapping(value = Array("/importTemplate"))
  @ResponseBody
  def importTemplate(): AjaxResult = {
    executeRequest responseAjaxResult {
      generateTemplate(classOf[SvnkitGroup], excelTitle)
    }
  }

  /**
    * 数据导入<br/>
    *
    * 数据导入流程：<br/>
    * 1、完成初始化操作，目的是实现服务器数据与数据库数据的同步<br/>
    * 2、获取上传的Excel文件中的数据，并转换成实体<br/>
    * 3、根据实体创建组别<br/>
    * 4、将数据保存到数据库，需要判断是否支持更新，如果支持更新则插入或更新，否则筛选出新建的组别进行插入<br/>
    */
  @RequiresPermissions(Array("svnkit:groups:import"))
  @RequestMapping(value = Array("/import/{serverKey}"))
  @ResponseBody
  def importUser(file: MultipartFile, updateSupport: Boolean, @PathVariable("serverKey") serverKey: String): AjaxResult = {
    executeRequest responseAjax {
      // 1、完成初始化操作，目的是实现服务器数据与数据库数据的同步
      init(serverKey)
      val server = svnkitServerService.selectById(serverKey)

      // 2、获取上传的Excel文件中的数据，并转换成实体
      val groups = importRecords(file, classOf[SvnkitGroup])

      if (groups.length < 1) {
        throw new Exception("导入的数据为空")
      }
      if (groups.groupBy(_.groupName).filter(_._2.size > 1).size > 0) {
        throw new Exception("导入的数据中存在相同组别名称的数据")
      }
      // 查询出现有的组别
      val existsGroups: util.List[SvnkitGroup] = groupService.selectList(new EntityWrapper[SvnkitGroup]().eq("server_key", serverKey).in("group_name", groups.map(_.groupName)))

      // 找出创建的组别
      val createGroups: util.List[SvnkitGroup] = groups.filter(group => {
        existsGroups.filter(exists => {
          exists.groupName == group.groupName
        }).size == 0
      }).map(group => {
        group.serverKey = serverKey
        group
      })

      createGroups.foreach(group => {
        // 3、根据实体创建组别
        info("开始创建组别")
        svnkitRemoteService.createGroup(server.serviceName, group.groupName)

        // 4、将数据保存到数据库
        info("保存到数据库")
        groupService.insert(group)
      })

      // 需要判断是否支持更新
      if (updateSupport) {
        // 更新已有数据
        existsGroups.filter(exists => {
          groups.filter(group => {
            exists.groupName == group.groupName && exists.serverKey == serverKey
          }).size > 0
        }).foreach(group => {
          groups.filter(_.groupName == group.groupName)
            .foreach(importGroup => {
              group.groupDesc = importGroup.groupDesc
              groupService.updateById(group)
            })
        })
      }
    }
  }

  /**
    * 新增界面
    */
  @RequiresPermissions(Array("svnkit:groups:add"))
  @GetMapping(Array("/add"))
  def add(mm: ModelMap): String = {
    PREFIX + "/add"
  }

  /**
    * 新增保存
    */
  @RequiresPermissions(Array("svnkit:groups:add"))
  @PostMapping(Array("/add"))
  @ResponseBody
  def addSave(record: SvnkitGroup): AjaxResult = {
    executeRequest responseAjax {
      val server = svnkitServerService.selectById(record.serverKey)
      // 创建组别
      svnkitRemoteService.createGroup(server.serviceName, record.groupName)
      // 插入组别
      groupService.insert(record)
    }
  }

  /**
    * 数据导出
    */
  @RequiresPermissions(Array("svnkit:groups:export"))
  @RequestMapping(value = Array("/export"))
  @ResponseBody
  def export(record: SvnkitGroup): AjaxResult = {
    executeRequest responseAjaxResult {
      val wrapper = new EntityWrapper[SvnkitGroup]()
        .like("server_key", record.serverKey)
        .like("group_name", record.groupName)
        .like("group_desc", record.groupDesc)
      exportRecords(groupService, wrapper, classOf[SvnkitGroup], excelTitle)
    }
  }

  /**
    * 编辑页面
    */
  @RequiresPermissions(Array("svnkit:groups:edit"))
  @GetMapping(Array("/edit/{id}"))
  def edit(@PathVariable("id") id: String, mm: ModelMap): String = {
    val group = groupService.selectById(id)
    val server = svnkitServerService.selectById(group.serverKey)
    mm.put("group", group)
    mm.put("allUsers", userService.selectList(new EntityWrapper[SvnkitUser]().eq("server_key", group.serverKey).orderBy("username")))
    mm.put("groupUsers", svnkitRemoteService.loadAllGroups(server.serviceName).getOrDefault(group.groupName, Array[String]()).toList)
    PREFIX + "/edit"
  }

  /**
    * 编辑保存<br/>
    *
    * 组别编辑保存逻辑：<br/>
    * 1、获取组别原有用户<br/>
    * 2、获取组别有权限访问的仓库<br/>
    *
    */
  @RequiresPermissions(Array("svnkit:groups:edit"))
  @PostMapping(Array("/edit"))
  @ResponseBody
  def editSave(group: SvnkitGroup, usernames: String): AjaxResult = {
    executeRequest responseAjax {
      val server = svnkitServerService.selectById(group.serverKey)
      val emailAccount = emailAccountService.selectById(server.sendEmail)

      // 1、获取组别原有用户
      val oldGroupUsers = svnkitRemoteService.loadAllGroups(server.serviceName).getOrDefault(group.groupName, Array[String]())

      // 2、获取组别有权限访问的仓库
      val haveRightRepositoryNames = svnkitRemoteService.groupHaveRightRepositories(server.serviceName, group.groupName)
      val haveRightRepositories: util.List[SvnkitRepository] = if (haveRightRepositoryNames.size() > 0) {
        repositoryService.selectList(new EntityWrapper[SvnkitRepository]().eq("server_key", server.rowKey).in("repository_name", haveRightRepositoryNames))
      } else {
        List[SvnkitRepository]()
      }

      // 3、设置组别用户
      svnkitRemoteService.setGroupUsers(server.serviceName, group.groupName, if (CommUtil.isEmpty(usernames)) "" else usernames)

      // 4、获取组别现有用户
      val newGroupUsers = svnkitRemoteService.loadAllGroups(server.serviceName).getOrDefault(group.groupName, Array[String]())

      // 5、维护组别信息
      groupService.updateById(group)

      // 6、发送邮件通知
      if (haveRightRepositories.size() > 0) {
        // 查询所有用户信息
        val allUserList = userService.selectList(new EntityWrapper[SvnkitUser]().eq("server_key", server.rowKey))

        // 组中新增的用户发送新增权限的邮件
        val addUserNames = newGroupUsers.filter(name => !oldGroupUsers.contains(name))
        val addUserLists = allUserList.filter(user => addUserNames.contains(user.username))
        if (CommUtil.isNotEmpty(emailAccount)) {
          addUserLists.filter(user => CommUtil.isNotEmpty(user.staffEmail)).foreach(user => {
            sendRepositoryRightUserEmail(server, emailAccount, group, user, haveRightRepositories, "GroupUserGrant")
          })

          // 组中移除的用户发送回收权限的邮件
          val delUserNames = oldGroupUsers.filter(name => !newGroupUsers.contains(name))
          val delUserLists = allUserList.filter(user => delUserNames.contains(user.username))
          delUserLists.filter(user => CommUtil.isNotEmpty(user.staffEmail)).foreach(user => {
            sendRepositoryRightUserEmail(server, emailAccount, group, user, haveRightRepositories, "GroupUserRevoke")
          })

          // 对仓库负责人发送权限变动的邮件
          haveRightRepositories.foreach(repository => {
            if (CommUtil.isNotEmpty(repository.contactsEmail)) {
              val lastUserNames = svnkitRemoteService.listRightUsers(server.serviceName, repository.repositoryName).keySet()
              val lastUserLists: util.List[SvnkitUser] = if (lastUserNames.size() > 0) {
                userService.selectList(new EntityWrapper[SvnkitUser]().eq("server_key", server.rowKey).in("username", lastUserNames))
              } else {
                List[SvnkitUser]()
              }
              sendRepositoryRightManagerEmail(server, emailAccount, repository, "GroupManagerModify", delUserLists, addUserLists, lastUserLists)
            }
          })
        }
      }
    }
  }


  /**
    * 发送仓库权限变更邮件给用户
    */
  def sendRepositoryRightUserEmail(server: SvnkitServer, email: EmailAccount, group: SvnkitGroup, user: SvnkitUser, repositories: util.List[SvnkitRepository], template: String): Unit = {
    // 邮件内容渲染
    val context = new Context()
    context.setVariable("contentTemplate", s"svnkit/email/${template}")

    context.setVariable("Username", user.staffName)
    context.setVariable("SVNGroup", group)

    context.setVariable("SVNRepositories", repositories)

    context.setVariable("SVNRepositoryRoot", if (server.serverAddress.endsWith("/")) server.serverAddress.substring(0, server.serverAddress.length - 1) else server.serverAddress)
    context.setVariable("AdminEmail", email.username)
    context.setVariable("AdminName", server.adminName)
    context.setVariable("SENDDatetime", DateUtil.formatShort(DateUtil.getCurrentDate))

    val content = templateEngine.process("svnkit/email/SvnkitEmailTemplate", context)

    // 发送邮件
    serviceEmailComponent.getEmailSender(email.rowKey).sendEmailByResource(Array(user.staffEmail), null, null, "SVN仓库权限变更", null, emailConfig, content)
  }

  /**
    * 发送仓库权限变更邮件给负责人
    */
  def sendRepositoryRightManagerEmail(server: SvnkitServer, email: EmailAccount, repository: SvnkitRepository, template: String, delUsers: util.List[SvnkitUser], addUsers: util.List[SvnkitUser], lastUsers: util.List[SvnkitUser]): Unit = {
    // 邮件内容渲染
    val context = new Context()
    context.setVariable("contentTemplate", s"svnkit/email/${template}")

    context.setVariable("Username", repository.contactsPerson)
    context.setVariable("SVNRepositoryDesc", repository.repositoryDesc)
    context.setVariable("SVNRepositoryName", repository.repositoryName)

    context.setVariable("SVNDelUsers", delUsers)
    context.setVariable("SVNAddUsers", addUsers)
    context.setVariable("SVNLastUsers", lastUsers)

    context.setVariable("SVNRepositoryRoot", if (server.serverAddress.endsWith("/")) server.serverAddress.substring(0, server.serverAddress.length - 1) else server.serverAddress)
    context.setVariable("AdminEmail", email.username)
    context.setVariable("AdminName", server.adminName)
    context.setVariable("SENDDatetime", DateUtil.formatShort(DateUtil.getCurrentDate))

    val content = templateEngine.process("svnkit/email/SvnkitEmailTemplate", context)

    // 发送邮件
    serviceEmailComponent.getEmailSender(email.rowKey).sendEmailByResource(Array(repository.contactsEmail), null, null, "SVN仓库权限变更", null, emailConfig, content)
  }
}
