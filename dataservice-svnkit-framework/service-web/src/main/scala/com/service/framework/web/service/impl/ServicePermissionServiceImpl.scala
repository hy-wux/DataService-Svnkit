package com.service.framework.web.service.impl

import com.service.framework.web.service.ServicePermissionService
import org.apache.shiro.SecurityUtils

/**
  * 权限控制的默认实现
  */
class ServicePermissionServiceImpl extends ServicePermissionService {
  /**
    * 判断是否具有该权限
    *
    * @param permission
    * @return
    */
  def hasPermission(permission: String): Boolean = {
    try {
      SecurityUtils.getSubject.isPermitted(permission)
    } catch {
      case _: Throwable => true
    }
  }

  /**
    * 当具有该权限时显示
    *
    * @param permission
    * @return
    */
  def displayWithPermission(permission: String): String = {
    try {
      if (hasPermission(permission)) {
        ""
      } else {
        "hidden"
      }
    } catch {
      case _: Throwable => ""
    }
  }
}
