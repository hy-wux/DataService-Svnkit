package com.service.visual.web.admin.modules.upms.controller

import com.baomidou.mybatisplus.mapper.EntityWrapper
import com.service.framework.core.utils.CommUtil
import com.service.framework.web.controller.{AjaxResult, ControllerInitBinder}
import com.service.framework.web.utils.FileUploadUtils
import com.service.visual.web.admin.core.utils.ShiroUtils
import com.service.visual.web.admin.core.web.controller.BaseController
import com.service.visual.web.admin.core.web.service.DictCodeService
import com.service.visual.web.admin.modules.system.entity.DictCode
import com.service.visual.web.admin.modules.upms.entity.{Role, User}
import com.service.visual.web.admin.modules.upms.service.{IDeptService, IRoleService, IUserService}
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.apache.shiro.crypto.SecureRandomNumberGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation._
import org.springframework.web.multipart.MultipartFile

import scala.collection.JavaConversions._

@Controller
@RequestMapping(Array("/upms/user"))
class UserController extends BaseController with ControllerInitBinder {
  private val prefix_admin = "upms/user/admin"
  private val prefix_profile = "upms/user/profile"

  @Autowired private val userService: IUserService = null

  @Autowired private val roleService: IRoleService = null

  @Autowired private val deptService: IDeptService = null

  @Autowired private val dictService: DictCodeService = null

  @RequiresPermissions(Array("upms:user:manage"))
  @GetMapping()
  def userIndex() = prefix_admin + "/list"

  /**
    * 获取用户列表
    */
  @RequiresPermissions(Array("upms:user:list"))
  @RequestMapping(value = Array("/list"))
  @ResponseBody
  def list(user: User): Any = {
    basePageQuery[User](userService, List(("deptid", "like", user.deptid), ("user_name", "like", user.userName), ("phone", "like", user.phone), ("email", "like", user.email)))
  }

  /**
    * 新增用户
    */
  @RequiresPermissions(Array("upms:user:add"))
  @GetMapping(Array("/add"))
  def add(mm: ModelMap): String = {
    mm.put("roles", roleService.selectByMap(null))
    prefix_admin + "/add"
  }

  /**
    * 校验账号
    */
  @PostMapping(Array("/checkAccountUnique"))
  @ResponseBody
  def checkAccountUnique(user: User): String = {
    var result: String = null
    if (CommUtil.isNotNull(user)) {
      result = s"${userService.selectCount(new EntityWrapper[User]().eq("user_account", user.userAccount).ne("row_key", user.rowKey))}"
    } else {
      result = "0"
    }
    result
  }

  /**
    * 校验邮箱
    */
  @PostMapping(Array("/checkEmailUnique"))
  @ResponseBody
  def checkEmailUnique(user: User): String = {
    var result: String = null
    if (CommUtil.isNotNull(user)) {
      result = s"${userService.selectCount(new EntityWrapper[User]().eq("email", user.email).ne("row_key", user.rowKey))}"
    } else {
      result = "0"
    }
    result
  }

  /**
    * 校验电话
    */
  @PostMapping(Array("/checkPhoneUnique"))
  @ResponseBody
  def checkPhoneUnique(user: User): String = {
    var result: String = null
    if (CommUtil.isNotNull(user)) {
      result = s"${userService.selectCount(new EntityWrapper[User]().eq("phone", user.phone).ne("row_key", user.rowKey))}"
    } else {
      result = "0"
    }
    result
  }

  /**
    * 新增保存用户
    */
  @RequiresPermissions(Array("upms:user:add"))
  @PostMapping(Array("/add"))
  @ResponseBody
  def addSave(user: User): AjaxResult = {
    try {
      // 替换密码盐
      user.userSalt = new SecureRandomNumberGenerator().nextBytes(5).toHex()
      // 加密密码
      user.setUserPass(ShiroUtils.md5(user.userPass, user.userSalt))
      userService.insert(user)
      success
    } catch {
      case ex: Exception => failure(ex.getMessage)
    }
  }

  @RequiresPermissions(Array("upms:user:delete"))
  @PostMapping(Array("/delete"))
  @ResponseBody
  def delete(ids: String): AjaxResult = {
    try {
      userService.deleteBatchIds(ids.split(",").toList)
      success
    } catch {
      case ex: Exception => failure(ex.getMessage)
    }
  }

  @RequiresPermissions(Array("upms:user:edit"))
  @GetMapping(Array("/edit/{id}"))
  def edit(@PathVariable("id") id: String, mm: ModelMap): String = {
    val user = userService.selectById(id)
    mm.put("user", user)
    mm.put("roles", roleService.selectList(new EntityWrapper[Role]()))
    mm.put("dept", deptService.selectById(user.deptid))
    prefix_admin + "/edit"
  }

  @RequiresPermissions(Array("upms:user:edit"))
  @PostMapping(Array("/edit"))
  @ResponseBody
  def editSave(user: User): AjaxResult = {
    try {
      userService.updateById(user)
      success
    } catch {
      case ex: Exception => failure(ex.getMessage)
    }
  }

  @RequiresPermissions(Array("upms:user:password"))
  @GetMapping(Array("/updatePassword/{id}"))
  def updatePassword(@PathVariable("id") id: String, mm: ModelMap): String = {
    val user = userService.selectById(id)
    mm.put("user", user)
    prefix_admin + "/password"
  }

  @RequiresPermissions(Array("upms:user:password"))
  @PostMapping(Array("/updatePassword"))
  @ResponseBody
  def updatePassword(user: User): AjaxResult = {
    try {
      // 替换密码盐
      user.userSalt = new SecureRandomNumberGenerator().nextBytes(5).toHex()
      // 加密密码
      user.setUserPass(ShiroUtils.md5(user.userPass, user.userSalt))
      userService.updateById(user)
      success
    } catch {
      case ex: Exception => failure(ex.getMessage)
    }
  }

  @GetMapping(Array("/profile"))
  def profile(mm: ModelMap): String = {
    val user = userService.selectById(ShiroUtils.getUser.id)
    user.setSex(dictService.selectDictCodeByType("sys_user_sex").toArray.map(x => x.asInstanceOf[DictCode]).filter(_.codeValue == user.sex).map(_.codeText).headOption.getOrElse(""))

    val dept = deptService.selectById(user.deptid)
    val roles = roleService.selectList(new EntityWrapper[Role]()).filter(role => CommUtil.isNotEmpty(role.roleName) && user.roleids.contains(role.rowKey)).map(_.roleName).mkString(",")

    mm.put("user", user)
    mm.put("dept", dept)
    mm.put("roles", roles)

    prefix_profile + "/profile"
  }

  /**
    * 编辑用户信息
    *
    * @param id
    * @param mm
    * @return
    */
  @GetMapping(Array("/profileEdit/{id}"))
  def profileEdit(@PathVariable("id") id: String, mm: ModelMap): String = {
    val user = userService.selectById(id)
    val dept = deptService.selectById(user.deptid)

    mm.put("user", user)
    mm.put("dept", dept)

    prefix_profile + "/edit"
  }

  /**
    * 保存用户信息
    *
    * @param user
    * @return
    */
  @PostMapping(Array("/profileEdit"))
  @ResponseBody
  def profileEdit(user: User): AjaxResult = {
    try {
      userService.updateById(user)
      success
    } catch {
      case ex: Exception => failure(ex.getMessage)
    }
  }

  /**
    * 修改密码
    *
    * @param id
    * @param mm
    * @return
    */
  @GetMapping(Array("/profileResetPassword/{id}"))
  def profileResetPassword(@PathVariable("id") id: String, mm: ModelMap): String = {
    val user = userService.selectById(id)

    mm.put("user", user)

    prefix_profile + "/password"
  }

  /**
    * 检查旧密码是否正确
    *
    * @param password
    * @return
    */
  @PostMapping(Array("/checkPassword"))
  @ResponseBody
  def checkPassword(userPass: String): Boolean = {
    val user = userService.selectById(ShiroUtils.getUser.id)
    ShiroUtils.md5(userPass, user.userSalt) == user.userPass
  }

  /**
    * 修改头像
    *
    * @param id
    * @param mm
    * @return
    */
  @GetMapping(Array("/profileAvatar/{id}"))
  def profileAvatar(@PathVariable("id") id: String, mm: ModelMap): String = {
    val user = userService.selectById(id)

    mm.put("user", user)

    prefix_profile + "/avatar"
  }

  /**
    * 保存头像
    */
  @PostMapping(Array("/updateAvatar"))
  @ResponseBody def updateAvatar(user: User, @RequestParam("avatarfile") file: MultipartFile): AjaxResult = {
    try {
      if (!file.isEmpty) {
        val avatar = FileUploadUtils.uploadAvatar(file)
        user.setAvatar(avatar)
        userService.updateById(user)
        ShiroUtils.getUser.avatar = avatar
        success
      } else {
        failure
      }
    } catch {
      case e: Exception => failure(e.getMessage)
    }
  }
}