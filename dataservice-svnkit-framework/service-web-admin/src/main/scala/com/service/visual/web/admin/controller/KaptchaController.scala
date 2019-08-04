package com.service.visual.web.admin.controller

import java.awt.image.BufferedImage
import java.io.IOException

import com.google.code.kaptcha.{Constants, Producer}
import com.service.visual.web.admin.ServiceWebAdminConfigurationProperties
import com.service.visual.web.admin.menus.KaptchaType
import javax.imageio.ImageIO
import javax.servlet.ServletOutputStream
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, RestController}

@RestController
@RequestMapping(Array("/kaptcha"))
class KaptchaController {

  @Autowired val producer: Producer = null
  @Autowired val properties: ServiceWebAdminConfigurationProperties = null

  /**
    * 生成验证码
    */
  @RequestMapping(value = Array(""), method = Array(RequestMethod.GET))
  def index(request: HttpServletRequest, response: HttpServletResponse): Unit = {
    val session = request.getSession

    response.setDateHeader("Expires", 0)
    response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate")
    response.addHeader("Cache-Control", "post-check=0, pre-check=0")
    response.setHeader("Pragma", "no-cache")

    response.setContentType("image/jpeg")

    var bi: BufferedImage = null

    properties.getKaptchaType match {
      case KaptchaType.mathkaptcha => {
        val mathText = producer.createText()
        bi = producer.createImage(mathText.split("@")(0))
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, mathText.split("@")(1))
      }
      case _ => {
        val charText = producer.createText()
        bi = producer.createImage(charText)
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, charText)
      }
    }

    var out: ServletOutputStream = null
    try {
      out = response.getOutputStream
    } catch {
      case e: IOException =>
        e.printStackTrace()
    }
    try {
      ImageIO.write(bi, "jpg", out)
    } catch {
      case e: IOException =>
        e.printStackTrace()
    }
    try {
      try {
        out.flush()
      } catch {
        case e: IOException =>
          e.printStackTrace()
      }
    } finally {
      try {
        out.close()
      } catch {
        case e: IOException =>
          e.printStackTrace()
      }
    }
  }
}
