package com.service.visual.web.admin.modules.upms.entity

import com.baomidou.mybatisplus.activerecord.Model
import com.baomidou.mybatisplus.annotations.TableName
import com.service.framework.jdbc.entity.{BaseEntity, RowKey}

import scala.beans.BeanProperty

/**
  * @author 伍鲜
  *
  *         菜单
  */
@TableName("sys_menu")
class Menu extends Model[Menu] with BaseEntity with RowKey {
  /**
    * 上级节点
    */
  @BeanProperty
  var pid: String = _
  /**
    * 菜单类型
    */
  @BeanProperty
  var menuType: String = _
  /**
    * 菜单名称
    */
  @BeanProperty
  var menuName: String = _
  /**
    * url地址
    */
  @BeanProperty
  var url: String = _
  /**
    * 权限标识
    */
  @BeanProperty
  var permission: String = _
  /**
    * 排序号
    */
  @BeanProperty
  var sortNumber: Int = 0
  /**
    * 图标
    */
  @BeanProperty
  var icon: String = _
  /**
    * 状态
    */
  @BeanProperty
  var menuStatus: String = _
  /**
    * 层级
    */
  @BeanProperty
  var levels: Int = 0
  /**
    * 备注
    */
  @BeanProperty
  var menuRemark: String = _
  /**
    * 模块
    */
  @BeanProperty
  var moduleName: String = _
}