package com.service.integrates.svnkit.admin.controller

import java.util

import com.baomidou.mybatisplus.mapper.EntityWrapper
import com.service.framework.core.utils.{CommUtil, DateUtil, EncryptUtil}
import com.service.framework.web.controller.{AjaxResult, ControllerInitBinder}
import com.service.framework.web.page.PageDomain
import com.service.integrates.easypoi.traits.ServiceEasypoiTrait
import com.service.integrates.email.entity.EmailAccount
import com.service.integrates.svnkit.admin.entity.{SvnkitGroup, SvnkitRepository, SvnkitServer, SvnkitUser}
import com.service.integrates.svnkit.admin.service.SvnkitRepositoryRemoteService
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation._
import org.springframework.web.multipart.MultipartFile
import org.thymeleaf.context.Context
import org.tmatesoft.svn.core.SVNNodeKind

import scala.collection.JavaConversions._

@Controller
@RequestMapping(Array("/service/svnkit/repositories"))
class SvnkitRepositoryController extends BaseController with ServiceEasypoiTrait with ControllerInitBinder {
  private val PREFIX = "svnkit/repositories"
  private val excelTitle = "SVN仓库列表"

  @Autowired val repositoryRemoteService: SvnkitRepositoryRemoteService = null

  @RequiresPermissions(Array("svnkit:repositories:manage"))
  @GetMapping()
  def repositoryIndex() = PREFIX + "/list"

  /**
    * 分页列表
    */
  @RequiresPermissions(Array("svnkit:repositories:list"))
  @RequestMapping(value = Array("/list"))
  @ResponseBody
  def list(record: SvnkitRepository): Any = {
    val wrapper = new EntityWrapper[SvnkitRepository]()
      .like("server_key", record.serverKey)
      .like("repository_name", record.repositoryName)
      .like("repository_desc", record.repositoryDesc)
    queryByPage[SvnkitRepository](repositoryService, wrapper)
  }

  /**
    * 初始化数据库
    */
  @RequiresPermissions(Array("svnkit:repositories:init"))
  @PostMapping(Array("/init"))
  @ResponseBody
  def init(serverKey: String): AjaxResult = {
    executeRequest responseAjax {
      if (CommUtil.isEmpty(serverKey)) {
        throw new Exception("请指定服务器")
      }
      val server = svnkitServerService.selectById(serverKey)
      val repositoryExists = svnkitRemoteService.listRepositories(server.serviceName)
      val repositoryLists = repositoryService.selectList(new EntityWrapper[SvnkitRepository]().eq("server_key", serverKey))

      // 添加存在的组别
      repositoryExists.filter(x => repositoryLists.filter(_.repositoryName == x).size == 0).foreach(x => {
        val repository = new SvnkitRepository
        repository.repositoryName = x
        repository.serverKey = serverKey
        repository.updateDatetime = DateUtil.getCurrentDate
        repositoryService.insert(repository)
      })
      // 删除不存在的组别
      repositoryLists.filter(x => !repositoryExists.contains(x.repositoryName)).foreach(x => {
        repositoryService.deleteById(x.rowKey)
      })
    }
  }

  /**
    * 数据导入模板
    */
  @RequiresPermissions(Array("svnkit:repositories:import"))
  @RequestMapping(value = Array("/importTemplate"))
  @ResponseBody
  def importTemplate(): AjaxResult = {
    executeRequest responseAjaxResult {
      generateTemplate(classOf[SvnkitRepository], excelTitle)
    }
  }

  /**
    * 数据导入<br/>
    *
    * 导入流程：<br/>
    * 1、完成初始化操作，目的是实现服务器数据与数据库数据的同步<br/>
    * 2、获取上传的Excel文件中的数据，并转换成实体<br/>
    * 3、根据实体创建仓库<br/>
    * 4、根据仓库类型创建目录<br/>
    * 5、将数据保存到数据库，需要判断是否支持更新，如果支持更新则插入或更新，否则筛选出新建的仓库进行插入<br/>
    * 6、对新创建的仓库发送邮件通知<br/>
    */
  @RequiresPermissions(Array("svnkit:repositories:import"))
  @RequestMapping(value = Array("/import/{serverKey}"))
  @ResponseBody
  def importRepository(file: MultipartFile, updateSupport: Boolean, @PathVariable("serverKey") serverKey: String): AjaxResult = {
    executeRequest responseAjax {
      // 1、完成初始化操作，目的是实现服务器数据与数据库数据的同步
      init(serverKey)
      val server = svnkitServerService.selectById(serverKey)
      val emailAccount = emailAccountService.selectById(server.sendEmail)

      val user = userService.selectOne(new EntityWrapper[SvnkitUser]().eq("server_key", serverKey).eq("login_account", onlineUserSession.getOnlineUserAccount))
      if (CommUtil.isEmpty(user)) throw new Exception("您还没有关联SVN用户")
      user.password = EncryptUtil.decrypt3DES(user.password)

      // 2、获取上传的Excel文件中的数据，并转换成实体
      info("上传并解析文件")
      val repositories = importRecords(file, classOf[SvnkitRepository])
      if (repositories.length < 1) {
        throw new Exception("导入的数据为空")
      }
      if (repositories.groupBy(_.repositoryName).filter(_._2.size > 1).size > 0) {
        throw new Exception("导入的数据中存在相同仓库名称的数据")
      }
      // 查询已存在的仓库
      val existsRepositories = repositoryService.selectList(new EntityWrapper[SvnkitRepository]().eq("server_key", serverKey).in("repository_name", repositories.map(_.repositoryName)))
      // 筛选出新创建的仓库
      val createRepositories = repositories.filter(repository => {
        existsRepositories.filter(exists => {
          exists.repositoryName == repository.repositoryName
        }).size == 0
      })

      val now = DateUtil.getCurrentDate

      if (createRepositories.size > 0) {
        // 3、根据实体创建仓库
        info("开始创建仓库")
        val right = new util.HashMap[String, String]()
        right.put(user.username, "rw")
        svnkitRemoteService.createLocalRepositoryBatch(server.serviceName, createRepositories.map(_.repositoryName), true, false, right)

        // 4、根据仓库类型创建目录
        info("开始创建目录")
        createRepositories.filter(_.repositoryType == "single").foreach(repository => {
          val url = s"${server.serverAddress}${if (server.serverAddress.endsWith("/")) "" else "/"}${repository.repositoryName}"
          repositoryRemoteService.createSubDirectory(user.username, user.password, url, List("branches", "tags", "trunk"))
        })

        info("保存到数据库")
        // 5、将数据保存到数据库，需要判断是否支持更新，如果支持更新则插入或更新，否则筛选出新建的仓库进行插入
        repositoryService.insertBatch(createRepositories.map(repository => {
          repository.serverKey = serverKey
          repository.createDatetime = now
          repository
        }))

        // 6、对新创建的仓库发送邮件通知
        if (CommUtil.isNotEmpty(emailAccount)) {
          info("通知相关人员")
          createRepositories.filter(repository => CommUtil.isNotEmpty(repository.contactsEmail)).foreach(sendRepositoryCreateEmail(server, emailAccount, _))
        }
      }
      if (updateSupport) {
        existsRepositories.filter(exists => {
          repositories.filter(repository => {
            exists.repositoryName == repository.repositoryName && exists.serverKey == serverKey
          }).size > 0
        }).foreach(repository => {
          repositories.filter(_.repositoryName == repository.repositoryName)
            .foreach(importRecord => {
              repository.repositoryDesc = importRecord.repositoryDesc
              repository.contactsPerson = importRecord.contactsPerson
              repository.contactsEmail = importRecord.contactsEmail
              repository.updateDatetime = now
              repositoryService.updateById(repository)
            })
        })
      }
    }
  }

  /**
    * 新增界面
    */
  @RequiresPermissions(Array("svnkit:repositories:add"))
  @GetMapping(Array("/add"))
  def add(): String = {
    PREFIX + "/add"
  }

  /**
    * 新增保存
    */
  @RequiresPermissions(Array("svnkit:repositories:add"))
  @PostMapping(Array("/add"))
  @ResponseBody
  def addSave(record: SvnkitRepository): AjaxResult = {
    executeRequest responseAjax {
      val server = svnkitServerService.selectById(record.serverKey)
      // 获取已存在的仓库
      val repositoryExists = svnkitRemoteService.listRepositories(server.serviceName)
      // 判断仓库是否存在
      if (repositoryExists.contains(record.repositoryName)) {
        throw new Exception("仓库已存在")
      } else {
        val emailAccount = emailAccountService.selectById(server.sendEmail)

        val user = userService.selectOne(new EntityWrapper[SvnkitUser]().eq("server_key", record.serverKey).eq("login_account", onlineUserSession.getOnlineUserAccount))
        if (CommUtil.isEmpty(user)) throw new Exception("您还没有关联SVN用户")
        user.password = EncryptUtil.decrypt3DES(user.password)

        // 1、通过本地服务创建仓库
        // 创建本地SVN仓库
        val right = new util.HashMap[String, String]()
        right.put(user.username, "rw")
        svnkitRemoteService.createLocalRepository(server.serviceName, record.repositoryName, true, false, right)

        // 2、通过远程服务创建目录
        if (record.repositoryType.equalsIgnoreCase("single")) {
          val url = s"${server.serverAddress}${if (server.serverAddress.endsWith("/")) "" else "/"}${record.repositoryName}"
          repositoryRemoteService.createSubDirectory(user.username, user.password, url, List("branches", "tags", "trunk"))
        }

        // 3、保存到数据库
        repositoryService.insert(record)

        // 4、发送邮件
        if (CommUtil.isNotEmpty(record.contactsEmail) && CommUtil.isNotEmpty(emailAccount)) {
          sendRepositoryCreateEmail(server, emailAccount, record)
        }
      }
    }
  }

  /**
    * 数据导出
    */
  @RequiresPermissions(Array("svnkit:repositories:export"))
  @RequestMapping(value = Array("/export"))
  @ResponseBody
  def export(record: SvnkitRepository): AjaxResult = {
    executeRequest responseAjaxResult {
      val wrapper = new EntityWrapper[SvnkitRepository]()
        .like("server_key", record.serverKey)
        .like("repository_name", record.repositoryName)
        .like("repository_desc", record.repositoryDesc)
      exportRecords(repositoryService, wrapper, classOf[SvnkitRepository], excelTitle)
    }
  }

  /**
    * 编辑页面
    */
  @RequiresPermissions(Array("svnkit:repositories:edit"))
  @GetMapping(Array("/edit/{id}"))
  def edit(@PathVariable("id") id: String, mm: ModelMap): String = {
    mm.put("repository", repositoryService.selectById(id))
    PREFIX + "/edit"
  }

  /**
    * 编辑保存
    */
  @RequiresPermissions(Array("svnkit:repositories:edit"))
  @PostMapping(Array("/edit"))
  @ResponseBody
  def editSave(repository: SvnkitRepository): AjaxResult = {
    executeRequest responseAjax {
      repositoryService.updateById(repository)
    }
  }

  /**
    * 浏览页面
    */
  @RequiresPermissions(Array("svnkit:repositories:view"))
  @GetMapping(Array("/repositoryView/{id}"))
  def repositoryView(@PathVariable("id") id: String, mm: ModelMap): String = {
    mm.put("repository", repositoryService.selectById(id))
    PREFIX + "/view"
  }

  /**
    * 仓库树，使用远程服务操作
    */
  @RequiresPermissions(Array("svnkit:repositories:view"))
  @RequestMapping(Array("/listChildrenView"))
  @ResponseBody
  def listChildrenView(serverKey: String, repositoryName: String, id: String): Any = {
    try {
      val result: util.List[util.Map[String, String]] = new util.ArrayList[util.Map[String, String]]()
      val server = svnkitServerService.selectById(serverKey)

      if (CommUtil.isEmpty(id)) {
        result.add(Map[String, String]("id" -> repositoryName, "name" -> repositoryName, "isParent" -> "true"))
      } else {
        val user = userService.selectOne(new EntityWrapper[SvnkitUser]().eq("server_key", serverKey).eq("login_account", onlineUserSession.getOnlineUserAccount))
        if (CommUtil.isEmpty(user)) throw new Exception("您还没有关联SVN用户")
        user.password = EncryptUtil.decrypt3DES(user.password)

        try {
          val url = s"${server.serverAddress}${if (server.serverAddress.endsWith("/")) "" else "/"}${id}"
          val entries = repositoryRemoteService.listEntries(user.username, user.password, url, "", SVNNodeKind.DIR, false)

          entries.foreach(entry => {
            val path = entry.getURL.getPath.substring(5)
            result.add(Map[String, String]("id" -> path, "pid" -> path.substring(0, path.lastIndexOf("/")), "name" -> entry.getName, "isParent" -> "true"))
          })
        } catch {
          case ex: Exception => result.add(Map[String, String]("id" -> "id", "pid" -> repositoryName, "name" -> ex.getMessage))
        }
      }
      result
    } catch {
      case ex: Exception => failure(ex.getMessage)
    }
  }

  /**
    * 仓库条目，使用远程服务操作
    */
  @RequiresPermissions(Array("svnkit:repositories:view"))
  @RequestMapping(Array("/listEntries"))
  @ResponseBody
  def listEntries(serverKey: String, repositoryPath: String, queryName: String): Any = {
    try {
      val map = new util.HashMap[String, Any]()
      if (CommUtil.isNotEmpty(repositoryPath)) {
        val result: util.List[util.Map[String, String]] = new util.ArrayList[util.Map[String, String]]()
        val server = svnkitServerService.selectById(serverKey)

        val user = userService.selectOne(new EntityWrapper[SvnkitUser]().eq("server_key", serverKey).eq("login_account", onlineUserSession.getOnlineUserAccount))
        if (CommUtil.isEmpty(user)) throw new Exception("您还没有关联SVN用户")
        user.password = EncryptUtil.decrypt3DES(user.password)

        try {
          val url = s"${server.serverAddress}${if (server.serverAddress.endsWith("/")) "" else "/"}${repositoryPath}"
          val entries = repositoryRemoteService.listEntries(user.username, user.password, url, "", null, false).filter(_.getName.contains(if (CommUtil.isNotEmpty(queryName)) queryName else "")).sortWith(_.getName < _.getName)

          if (entries.size > 0) {
            val users = userService.selectList(new EntityWrapper[SvnkitUser]().eq("server_key", serverKey).in("username", entries.map(_.getAuthor)))

            val domain = PageDomain.buildPageDomain()
            (Math.max((domain.current - 1) * domain.size, 0) until Math.min(domain.current * domain.size, entries.size)).map(entries(_)).foreach(entry => {
              val user = users.filter(_.username == entry.getAuthor).headOption.getOrElse(null)
              if (CommUtil.isNotNull(user)) {
                result.add(Map[String, String](
                  "name" -> entry.getName,
                  "version" -> entry.getRevision().toString,
                  "lastUser" -> entry.getAuthor,
                  "staffName" -> user.staffName,
                  "staffNum" -> user.staffNum,
                  "staffEmail" -> user.staffEmail,
                  "lastTime" -> DateUtil.formatLong(entry.getDate))
                )
              } else {
                result.add(Map[String, String](
                  "name" -> entry.getName,
                  "version" -> entry.getRevision().toString,
                  "lastUser" -> entry.getAuthor,
                  "staffName" -> "",
                  "staffNum" -> "",
                  "staffEmail" -> "",
                  "lastTime" -> DateUtil.formatLong(entry.getDate))
                )
              }
            })
          }

          map.put("total", entries.size())
          map.put("rows", result)
        } catch {
          case _: Throwable => /* ignore */
        }
      }
      map.put("code", 200)
      map
    } catch {
      case ex: Exception => failure(ex.getMessage)
    }
  }

  /**
    * 权限页面
    */
  @RequiresPermissions(Array("svnkit:repositories:right"))
  @GetMapping(Array("/repositoryRight/{id}"))
  def repositoryRight(@PathVariable("id") id: String, mm: ModelMap): String = {
    mm.put("repository", repositoryService.selectById(id))
    PREFIX + "/right"
  }

  /**
    * 条目权限，使用本地服务操作
    */
  @RequiresPermissions(Array("svnkit:repositories:right"))
  @RequestMapping(Array("/listRights"))
  @ResponseBody
  def listRights(serverKey: String, repositoryName: String, repositoryPath: String, queryName: String): Any = {
    try {
      val resultMap = new util.HashMap[String, Any]()
      if (CommUtil.isNotEmpty(repositoryPath)) {
        val server = svnkitServerService.selectById(serverKey)
        // 保存实际返回的数据
        val result: util.List[util.Map[String, String]] = new util.ArrayList[util.Map[String, String]]()
        // 保存权限信息
        val rights: util.Map[String, util.Map[String, String]] = svnkitRemoteService.listRights(server.serviceName, repositoryName, if (repositoryPath.indexOf("/") != -1) repositoryPath.substring(repositoryPath.indexOf("/")) else "/")

        val users: List[SvnkitUser] = if (rights.keySet().filter(!_.startsWith("@")).size > 0) {
          userService.selectList(new EntityWrapper[SvnkitUser]().eq("server_key", serverKey).in("username", rights.keySet().filter(!_.startsWith("@"))))
            .filter(user => {
              if (CommUtil.isNotEmpty(queryName)) {
                // 根据各项条件模糊匹配
                user.username.contains(queryName) || (if (CommUtil.isNotEmpty(user.staffName)) user.staffName else "").contains(queryName) || (if (CommUtil.isNotEmpty(user.staffNum)) user.staffNum else "").contains(queryName) || (if (CommUtil.isNotEmpty(user.staffEmail)) user.staffEmail else "").contains(queryName)
              } else {
                // 不带查询条件
                true
              }
            })
            .toList
        } else {
          List[SvnkitUser]()
        }

        val groups: List[SvnkitGroup] = if (rights.keySet().filter(_.startsWith("@")).size > 0) {
          groupService.selectList(new EntityWrapper[SvnkitGroup]().eq("server_key", serverKey).in("group_name", rights.keySet().filter(_.startsWith("@"))))
            .filter(group => {
              if (CommUtil.isNotEmpty(queryName)) {
                // 根据各项条件模糊匹配
                group.groupName.contains(queryName) || (if (CommUtil.isNotEmpty(group.groupDesc)) group.groupDesc else "").contains(queryName)
              } else {
                // 不带查询条件
                true
              }
            })
            .toList
        } else {
          List[SvnkitGroup]()
        }

        val sortRights = rights.keySet().toList.filter(key => {
          if (CommUtil.isNotEmpty(queryName)) {
            // 带查询条件
            if (key.contains(queryName)) {
              // 名称匹配
              true
            } else {
              // 其他条件匹配
              users.filter(_.username == key).size > 0 || groups.filter(_.groupName.contains(key)).size > 0
            }
          } else {
            // 不带查询条件
            true
          }
        }).sortWith(_ < _)

        try {
          val domain = PageDomain.buildPageDomain()

          (Math.max((domain.current - 1) * domain.size, 0) until Math.min(domain.current * domain.size, sortRights.size)).map(sortRights(_)).foreach(key => {
            val isGroup = key.startsWith("@")

            val user = if (!isGroup) users.filter(_.username == key).headOption.getOrElse(null) else null
            val group = if (isGroup) groups.filter(_.groupName == key.substring(1)).headOption.getOrElse(null) else null

            val map = new util.HashMap[String, String]()
            map.put("right", rights.get(key).map(_._1).headOption.getOrElse(""))
            map.put("grant", rights.get(key).map(_._2).headOption.getOrElse(""))
            map.put("delete", (if (rights.get(key).map(_._2).headOption.getOrElse("") == "self") "" else "hidden"))

            if (isGroup) {
              map.put("name", key.substring(1))
              map.put("type", "group")

              if (CommUtil.isNotNull(group)) {
                map.put("objectName", group.groupDesc)
              } else {
                map.put("objectName", "")
              }
            } else {
              map.put("name", key)
              map.put("type", "user")
              if (CommUtil.isNotNull(user)) {
                map.put("objectName", user.staffName)
                map.put("staffNum", user.staffNum)
                map.put("staffEmail", user.staffEmail)
              } else {
                map.put("objectName", "")
                map.put("staffNum", "")
                map.put("staffEmail", "")
              }
            }

            result.add(map)
          })

          resultMap.put("total", sortRights.size)
          resultMap.put("rows", result)
        } catch {
          case _: Throwable => /* ignore */
        }
      }
      resultMap.put("code", 200)
      resultMap
    } catch {
      case ex: Exception => failure(ex.getMessage)
    }
  }

  /**
    * 权限新增
    */
  @RequiresPermissions(Array("svnkit:repositories:right"))
  @GetMapping(Array("/insertRight"))
  def insertRight(serverKey: String, repositoryName: String, repositoryPath: String, mm: ModelMap): String = {
    mm.put("serverKey", serverKey)
    mm.put("repositoryName", repositoryName)
    mm.put("repositoryPath", if (repositoryPath.indexOf("/") != -1) repositoryPath.substring(repositoryPath.indexOf("/")) else "/")
    mm.put("allUsers", userService.selectList(new EntityWrapper[SvnkitUser]().eq("server_key", serverKey).orderBy("username")))
    mm.put("allGroups", groupService.selectList(new EntityWrapper[SvnkitGroup]().eq("server_key", serverKey).orderBy("group_name")))
    PREFIX + "/rightadd"
  }

  /**
    * 权限添加<br/>
    *
    * 权限添加流程：<br/>
    * 1、获取仓库的初始权限配置<br/>
    * 2、授予对象访问权限<br/>
    * 3、获取仓库的最终权限配置<br/>
    * 4、如果是仓库的根路径，则通知用户权限的变动<br/>
    */
  @RequiresPermissions(Array("svnkit:repositories:right"))
  @PostMapping(Array("/rightSave"))
  @ResponseBody
  def rightSave(serverKey: String, repositoryName: String, repositoryPath: String, groups: String, users: String, right: String): AjaxResult = {
    executeRequest responseAjax {
      val server = svnkitServerService.selectById(serverKey)
      val emailAccount = emailAccountService.selectById(server.sendEmail)
      // 1、获取仓库的初始权限配置
      val repository = repositoryService.selectOne(new EntityWrapper[SvnkitRepository]().eq("server_key", serverKey).eq("repository_name", repositoryName))
      val oldUserRights: util.Map[String, String] = svnkitRemoteService.listRightUsers(server.serviceName, repositoryName)

      // 2、授予对象访问权限
      val keys = s"${if (CommUtil.isNotEmpty(groups)) groups else ""}".split(",").filter(_ != "").map(group => s"@${group}").++(s"${if (CommUtil.isNotEmpty(users)) users else ""}".split(",").filter(_ != ""))
      if (keys.length > 0) {
        val rights = new util.HashMap[String, String]()
        keys.foreach(key => rights.put(key, right))
        svnkitRemoteService.addRights(server.serviceName, repositoryName, repositoryPath, rights)
      }

      // 3、获取仓库的最终权限配置
      val newUserRights: util.Map[String, String] = svnkitRemoteService.listRightUsers(server.serviceName, repositoryName)
      val userList = userService.selectList(new EntityWrapper[SvnkitUser]().eq("server_key", serverKey).in("username", newUserRights.keySet()))

      // 4、如果是仓库的根路径，则通知用户权限的变动
      if (repositoryPath == "/") {
        // 新增加的用户名称
        val grantUserNames = newUserRights.filter(n => !oldUserRights.contains(n._1)).map(_._1)

        // 权限改变的用户权限
        val changeUserRights = oldUserRights.filter(o => newUserRights.getOrDefault(o._1, "") != o._2)

        if (CommUtil.isNotEmpty(emailAccount)) {
          // 对新增的用户发送回收权限的邮件通知
          userList.filter(user => grantUserNames.contains(user.username)).filter(user => CommUtil.isNotEmpty(user.staffEmail)).foreach(user => {
            sendRepositoryRightUserEmail(server, emailAccount, repository, user, "RepositoryUserGrant", newUserRights.getOrDefault(user.username, ""))
          })
          // 对权限改变的用户发送权限改变的邮件通知
          userList.filter(user => changeUserRights.contains(user.username)).filter(user => CommUtil.isNotEmpty(user.staffEmail)).foreach(user => {
            sendRepositoryRightUserEmail(server, emailAccount, repository, user, "RepositoryUserModify", newUserRights.getOrDefault(user.username, ""))
          })
          // 对项目负责人发送有权限访问仓库的用户列表的邮件
          if (CommUtil.isNotEmpty(repository.contactsEmail) && grantUserNames.size > 0) {
            sendRepositoryRightManagerEmail(server, emailAccount, repository, "RepositoryManagerUserGrant", userList.filter(user => grantUserNames.contains(user.username)).toList, userList.filter(user => newUserRights.contains(user.username)).toList)
          }
        }
      }
    }
  }

  /**
    * 权限修改
    */
  @RequiresPermissions(Array("svnkit:repositories:right"))
  @GetMapping(Array("/updateRight"))
  def updateRight(serverKey: String, repositoryName: String, repositoryPath: String, objectName: String, types: String, mm: ModelMap): String = {
    mm.put("serverKey", serverKey)
    mm.put("repositoryName", repositoryName)
    mm.put("repositoryPath", if (repositoryPath.indexOf("/") != -1) repositoryPath.substring(repositoryPath.indexOf("/")) else "/")
    val server = svnkitServerService.selectById(serverKey)
    val rights = svnkitRemoteService.listRights(server.serviceName, repositoryName, if (repositoryPath.indexOf("/") != -1) repositoryPath.substring(repositoryPath.indexOf("/")) else "/")
    if (types == "group") {
      mm.put("groups", objectName)
      mm.put("right", rights.get(s"@${objectName}").map(_._1).headOption.getOrElse(""))
    } else {
      mm.put("users", objectName)
      mm.put("right", rights.get(objectName).map(_._1).headOption.getOrElse(""))
    }
    mm.put("allUsers", userService.selectList(new EntityWrapper[SvnkitUser]().eq("server_key", serverKey).orderBy("username")))
    mm.put("allGroups", groupService.selectList(new EntityWrapper[SvnkitGroup]().eq("server_key", serverKey).orderBy("group_name")))
    PREFIX + "/rightedit"
  }

  /**
    * 权限删除<br/>
    *
    * 权限删除流程：<br/>
    * 1、获取仓库的初始权限配置<br/>
    * 2、删除直接授予的权限<br/>
    * 3、获取仓库的最终权限配置<br/>
    * 4、如果是仓库的根路径，则通知用户权限的变动<br/>
    */
  @RequiresPermissions(Array("svnkit:repositories:right"))
  @PostMapping(Array("/deleteRight"))
  @ResponseBody
  def deleteRight(serverKey: String, repositoryName: String, repositoryPath: String, objectName: String, types: String): AjaxResult = {
    executeRequest responseAjax {
      val server = svnkitServerService.selectById(serverKey)
      val emailAccount = emailAccountService.selectById(server.sendEmail)
      // 1、获取仓库的初始权限配置
      val repository = repositoryService.selectOne(new EntityWrapper[SvnkitRepository]().eq("server_key", serverKey).eq("repository_name", repositoryName))
      val oldUserRights: util.Map[String, String] = svnkitRemoteService.listRightUsers(server.serviceName, repositoryName)
      val userList = userService.selectList(new EntityWrapper[SvnkitUser]().eq("server_key", serverKey).in("username", oldUserRights.keySet()))

      // 2、删除直接授予的权限
      svnkitRemoteService.deleteRights(server.serviceName, repositoryName, if (repositoryPath.indexOf("/") != -1) repositoryPath.substring(repositoryPath.indexOf("/")) else "/", Array(if (types == "group") s"@${objectName}" else objectName))

      // 3、获取仓库的最终权限配置
      val newUserRights: util.Map[String, String] = svnkitRemoteService.listRightUsers(server.serviceName, repositoryName)

      // 4、如果是仓库的根路径，则通知用户权限的变动
      if (repositoryPath.indexOf("/") == -1) {
        // 被删除的用户名称
        val deleteUserNames = oldUserRights.filter(o => !newUserRights.contains(o._1)).map(_._1)

        // 权限改变的用户权限
        val changeUserRights = newUserRights.filter(n => oldUserRights.getOrDefault(n._1, "") != n._2)

        if (CommUtil.isNotEmpty(emailAccount)) {
          // 对删除的用户发送回收权限的邮件通知
          userList.filter(user => deleteUserNames.contains(user.username)).filter(user => CommUtil.isNotEmpty(user.staffEmail)).foreach(user => {
            sendRepositoryRightUserEmail(server, emailAccount, repository, user, "RepositoryUserRevoke", "")
          })
          // 对权限改变的用户发送权限改变的邮件通知
          userList.filter(user => changeUserRights.contains(user.username)).filter(user => CommUtil.isNotEmpty(user.staffEmail)).foreach(user => {
            sendRepositoryRightUserEmail(server, emailAccount, repository, user, "RepositoryUserModify", newUserRights.getOrDefault(user.username, ""))
          })
          // 对项目负责人发送有权限访问仓库的用户列表的邮件
          if (CommUtil.isNotEmpty(repository.contactsEmail) && deleteUserNames.size > 0) {
            sendRepositoryRightManagerEmail(server, emailAccount, repository, "RepositoryManagerUserRevoke", userList.filter(user => deleteUserNames.contains(user.username)).toList, userList.filter(user => newUserRights.contains(user.username)).toList)
          }
        }
      }
    }
  }

  /* 邮件服务 */

  /**
    * 发送仓库创建的邮件
    *
    * @param repository
    */
  def sendRepositoryCreateEmail(server: SvnkitServer, email: EmailAccount, repository: SvnkitRepository): Unit = {
    // 邮件内容渲染
    val context = new Context()
    context.setVariable("contentTemplate", "svnkit/email/RepositoryCreate")

    context.setVariable("Username", repository.contactsPerson)
    context.setVariable("SVNRepositoryDesc", repository.repositoryDesc)
    context.setVariable("SVNRepositoryName", repository.repositoryName)

    context.setVariable("SVNRepositoryRoot", if (server.serverAddress.endsWith("/")) server.serverAddress.substring(0, server.serverAddress.length - 1) else server.serverAddress)
    context.setVariable("AdminEmail", email.username)
    context.setVariable("AdminName", server.adminName)
    context.setVariable("SENDDatetime", DateUtil.formatShort(DateUtil.getCurrentDate))

    val content = templateEngine.process("svnkit/email/SvnkitEmailTemplate", context)

    // 发送邮件
    serviceEmailComponent.getEmailSender(email.rowKey).sendEmailByResource(Array(repository.contactsEmail), null, null, "SVN仓库创建成功", null, emailConfig, content)
  }

  /**
    * 发送仓库权限变更邮件给用户
    *
    * @param repository
    */
  def sendRepositoryRightUserEmail(server: SvnkitServer, email: EmailAccount, repository: SvnkitRepository, user: SvnkitUser, template: String, right: String): Unit = {
    // 邮件内容渲染
    val context = new Context()
    context.setVariable("contentTemplate", s"svnkit/email/${template}")

    context.setVariable("Username", user.staffName)

    context.setVariable("SVNRepositoryName", repository.repositoryName)
    context.setVariable("SVNRepositoryDesc", repository.repositoryDesc)

    context.setVariable("SVNRepositoryRight", if (right == "rw") "读写" else if (right == "r") "只读" else "无访问权限")

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
    *
    * @param repository
    */
  def sendRepositoryRightManagerEmail(server: SvnkitServer, email: EmailAccount, repository: SvnkitRepository, template: String, changeUsers: util.List[SvnkitUser], lastUsers: util.List[SvnkitUser]): Unit = {
    // 邮件内容渲染
    val context = new Context()
    context.setVariable("contentTemplate", s"svnkit/email/${template}")

    context.setVariable("Username", repository.contactsPerson)
    context.setVariable("SVNRepositoryDesc", repository.repositoryDesc)
    context.setVariable("SVNRepositoryName", repository.repositoryName)

    context.setVariable("SVNChangeUsers", changeUsers)
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
