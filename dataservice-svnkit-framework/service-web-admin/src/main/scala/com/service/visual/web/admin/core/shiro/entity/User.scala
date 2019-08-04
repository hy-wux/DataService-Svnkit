package com.service.visual.web.admin.core.shiro.entity

import java.util.Date

import com.baomidou.mybatisplus.activerecord.Model
import com.baomidou.mybatisplus.annotations.TableName
import com.service.framework.jdbc.entity.{BaseEntity, RowKey}

import scala.beans.BeanProperty

/**
  * @author 伍鲜
  *
  *         用户
  */
@TableName("sys_user")
class User extends Model[User] with BaseEntity with RowKey {
  /**
    * 头像
    */
  @BeanProperty
  var avatar: String = _
  /**
    * 账号
    */
  @BeanProperty
  var userAccount: String = _
  /**
    * 密码
    */
  @BeanProperty
  var userPass: String = _
  /**
    * md5密码盐
    */
  @BeanProperty
  var userSalt: String = _
  /**
    * 名字
    */
  @BeanProperty
  var userName: String = _
  /**
    * 生日
    */
  @BeanProperty
  var birthday: Date = _
  /**
    * 性别
    */
  @BeanProperty
  var sex: String = _
  /**
    * 电子邮件
    */
  @BeanProperty
  var email: String = _
  /**
    * 电话
    */
  @BeanProperty
  var phone: String = _
  /**
    * 角色id
    */
  @BeanProperty
  var roleids: String = _
  /**
    * 部门id
    */
  @BeanProperty
  var deptid: String = _
  /**
    * 状态
    */
  @BeanProperty
  var userStatus: String = _
}