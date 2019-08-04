package com.service.visual.web.admin.modules.session.entity

import java.io.Serializable
import java.util.Date

import com.baomidou.mybatisplus.activerecord.Model
import com.baomidou.mybatisplus.annotations.{TableId, TableName}
import com.service.framework.jdbc.entity.BaseEntity

import scala.beans.BeanProperty

@TableName("sys_user_online")
class SysUserOnline extends Model[SysUserOnline] with BaseEntity {
  @TableId
  @BeanProperty var sessionId: String = _
  @BeanProperty var account: String = _
  @BeanProperty var deptName: String = _
  @BeanProperty var ipAddress: String = _
  @BeanProperty var loginLocation: String = _
  @BeanProperty var browser: String = _
  @BeanProperty var os: String = _
  @BeanProperty var status: String = _
  @BeanProperty var startTimestamp: Date = _
  @BeanProperty var lastAccessTime: Date = _
  @BeanProperty var expireTime: Int = _

  def pkVal: Serializable = sessionId
}
