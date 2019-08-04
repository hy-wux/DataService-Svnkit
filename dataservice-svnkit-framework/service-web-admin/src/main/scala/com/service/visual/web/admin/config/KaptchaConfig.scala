package com.service.visual.web.admin.config

import java.util.Properties

import com.google.code.kaptcha.impl.DefaultKaptcha
import com.google.code.kaptcha.util.Config
import com.service.framework.core.logs.Logging
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.{Bean, Configuration}

@Configuration
class KaptchaConfig extends Logging {

  /**
    * 字符验证码
    */
  @Bean
  @ConditionalOnProperty(value = Array("service.visual.web.kaptcha-type"), havingValue = "charKaptcha")
  def charKaptcha: DefaultKaptcha = {
    val properties = new Properties
    properties.put("kaptcha.border", "no")
    properties.put("kaptcha.border.color", "105,179,90")
    properties.put("kaptcha.image.width", "125")
    properties.put("kaptcha.image.height", "55")
    properties.put("kaptcha.session.key", "kaptchaCode")
    properties.put("kaptcha.textproducer.char.length", "5")
    properties.put("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑")
    properties.put("kaptcha.textproducer.font.size", "35")
    properties.put("kaptcha.textproducer.font.color", "blue")
    properties.put("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.ShadowGimpy")
    val config = new Config(properties)
    val defaultKaptcha = new DefaultKaptcha
    defaultKaptcha.setConfig(config)
    defaultKaptcha
  }

  /**
    * 数学验证码
    */
  @Bean
  @ConditionalOnProperty(value = Array("service.visual.web.kaptcha-type"), havingValue = "mathKaptcha")
  def mathKaptcha: DefaultKaptcha = {
    val properties = new Properties
    properties.put("kaptcha.border", "no")
    properties.put("kaptcha.border.color", "105,179,90")
    properties.put("kaptcha.image.width", "125")
    properties.put("kaptcha.image.height", "55")
    properties.put("kaptcha.session.key", "kaptchaCode")

    // 验证码文本生成器
    properties.setProperty("kaptcha.textproducer.impl", "com.service.visual.web.admin.config.KaptchaTextCreator")
    // 干扰实现类
    properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise")

    properties.put("kaptcha.textproducer.char.length", "6")
    properties.put("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑")
    properties.put("kaptcha.textproducer.font.size", "35")
    properties.put("kaptcha.textproducer.font.color", "blue")
    properties.put("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.ShadowGimpy")
    val config = new Config(properties)
    val defaultKaptcha = new DefaultKaptcha
    defaultKaptcha.setConfig(config)
    defaultKaptcha
  }
}
