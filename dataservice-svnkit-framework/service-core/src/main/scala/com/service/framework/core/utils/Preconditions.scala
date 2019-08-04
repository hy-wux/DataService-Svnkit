package com.service.framework.core.utils

/**
  * @author 伍鲜
  */
class Preconditions {

}

/**
  * @author 伍鲜
  */
object Preconditions {
  def isNotNull[T](reference: T, errorMessage: Any = null): T = {
    if (CommUtil.isNull(reference)) {
      throw new NullPointerException(String.valueOf(errorMessage))
    } else {
      reference
    }
  }

  def isNotEmpty[T](reference: T, errorMessage: Any = null): T = {
    if (CommUtil.isEmpty(reference)) {
      throw new NullPointerException(String.valueOf(errorMessage))
    } else {
      reference
    }
  }

  def getOrElse[T](reference: T, default: T): T = {
    if (reference == null) {
      default
    } else {
      reference
    }
  }
}
