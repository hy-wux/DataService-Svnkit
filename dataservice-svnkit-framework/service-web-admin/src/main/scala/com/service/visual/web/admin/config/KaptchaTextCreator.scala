package com.service.visual.web.admin.config

import java.util.Random

import com.google.code.kaptcha.text.impl.DefaultTextCreator

class KaptchaTextCreator extends DefaultTextCreator {
  private val numbers = "0,1,2,3,4,5,6,7,8,9,10".split(",")

  override def getText: String = {
    var result: Int = 0
    val random = new Random
    val x = random.nextInt(10)
    val y = random.nextInt(10)
    val builder = new StringBuilder
    val randomOperationRands = (Math.random * 3).round.toInt
    if (randomOperationRands == 0) { // 乘法
      result = x * y // 求乘积
      builder.append(numbers(x))
      builder.append("*")
      builder.append(numbers(y))
    } else if (randomOperationRands == 1) { // 除法
      if (x != 0 && y % x == 0) {
        result = y / x // 求商
        builder.append(numbers(y))
        builder.append("/")
        builder.append(numbers(x))
      } else {
        result = y % x // 求余
        builder.append(numbers(y))
        builder.append("%")
        builder.append(numbers(x))
      }
    } else if (randomOperationRands == 2) { // 减法
      if (x >= y) {
        result = x - y
        builder.append(numbers(x))
        builder.append("-")
        builder.append(numbers(y))
      } else {
        result = y - x
        builder.append(numbers(y))
        builder.append("-")
        builder.append(numbers(x))
      }
    } else { // 加法
      result = x + y
      builder.append(numbers(x))
      builder.append("+")
      builder.append(numbers(y))
    }
    builder.append("=?@" + result)
    builder.toString
  }
}
