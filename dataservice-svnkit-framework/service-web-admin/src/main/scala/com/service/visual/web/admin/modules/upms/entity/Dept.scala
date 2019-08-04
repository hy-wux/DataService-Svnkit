package com.service.visual.web.admin.modules.upms.entity

import com.baomidou.mybatisplus.activerecord.Model
import com.baomidou.mybatisplus.annotations.TableName
import com.service.framework.jdbc.entity.{BaseEntity, RowKey}

import scala.beans.BeanProperty

/**
  * @author 伍鲜
  *
  *         部门
  */
@TableName("sys_dept")
class Dept extends Model[Dept] with BaseEntity with RowKey {
  /**
    * 上级节点
    */
  @BeanProperty
  var pid: String = _
  /**
    * 名称
    */
  @BeanProperty
  var deptName: String = _
  /**
    * 排序号
    */
  @BeanProperty
  var sortNumber: Int = 0
  /**
    * 负责人
    */
  @BeanProperty
  var leader: String = _
  /**
    * 联系电话
    */
  @BeanProperty
  var phone: String = _
  /**
    * 电子邮箱
    */
  @BeanProperty
  var email: String = _
  /**
    * 联系地址
    */
  @BeanProperty
  var address: String = _
  /**
    * 状态
    */
  @BeanProperty
  var deptStatus: String = _
  /**
    * 备注
    */
  @BeanProperty
  var deptRemark: String = _
  /**
    * 模块
    */
  @BeanProperty
  var moduleName: String = _
}