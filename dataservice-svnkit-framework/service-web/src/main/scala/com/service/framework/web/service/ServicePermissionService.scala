package com.service.framework.web.service

trait ServicePermissionService {
  /**
    * 判断是否具有该权限
    *
    * @param permission
    * @return
    */
  def hasPermission(permission: String): Boolean

  /**
    * 当具有该权限时显示
    *
    * @param permission
    * @return
    */
  def displayWithPermission(permission: String): String
}
