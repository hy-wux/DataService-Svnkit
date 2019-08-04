package com.service.visual.web.admin.entity

import java.util

import scala.beans.BeanProperty

/**
  * @author 伍鲜
  *
  *         菜单树
  */
class MenuNode {

  /**
    * 节点id
    */
  @BeanProperty
  var id: String = null

  /**
    * 父节点
    */
  @BeanProperty
  var pid: String = null

  /**
    * 节点名称
    */
  @BeanProperty
  var name: String = null

  /**
    * 按钮的排序
    */
  @BeanProperty
  var sortNumber: Integer = null

  /**
    * 按钮级别
    */
  @BeanProperty
  var levels: Integer = null

  /**
    * 节点的url
    */
  @BeanProperty
  var url: String = null

  /**
    * 节点图标
    */
  @BeanProperty
  var icon: String = null

  /**
    * 子节点的集合
    */
  @BeanProperty
  var children: util.List[MenuNode] = new util.ArrayList[MenuNode]()

  /**
    * 查询子节点时候的临时集合
    */
  @BeanProperty
  val linkedList: util.List[MenuNode] = new util.ArrayList[MenuNode]()

}
