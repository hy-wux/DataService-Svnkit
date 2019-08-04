package com.service.framework.core.component

import org.springframework.beans.factory.support.{BeanDefinitionBuilder, BeanDefinitionRegistry, DefaultListableBeanFactory}
import org.springframework.context.{ApplicationContext, ApplicationContextAware}
import org.springframework.stereotype.Component

/**
  * @author 伍鲜
  */
@Component
class SpringContextHolder extends ApplicationContextAware {
  override def setApplicationContext(applicationContext: ApplicationContext): Unit = {
    SpringContextHolder.applicationContext = applicationContext
    SpringContextHolder.beanDefinitionRegistry = applicationContext.getAutowireCapableBeanFactory.asInstanceOf[DefaultListableBeanFactory]
  }
}

object SpringContextHolder {
  var applicationContext: ApplicationContext = _
  var beanDefinitionRegistry: BeanDefinitionRegistry = _

  /**
    * 手工注册Bean
    *
    * @param beanName
    * @param beanClass
    */
  def registerBean(beanName: String, beanClass: String): Unit = {
    val builder = BeanDefinitionBuilder.genericBeanDefinition(beanClass)
    beanDefinitionRegistry.registerBeanDefinition(beanName, builder.getBeanDefinition)
  }

  /**
    * 手工注册Bean
    *
    * @param beanName
    * @param beanClass
    */
  def registerBean(beanName: String, beanClass: Class[_]): Unit = {
    val builder = BeanDefinitionBuilder.genericBeanDefinition(beanClass)
    beanDefinitionRegistry.registerBeanDefinition(beanName, builder.getBeanDefinition)
  }

  /**
    * 手工移除Bean
    *
    * @param beanName
    */
  def unregisterBean(beanName: String): Unit = {
    beanDefinitionRegistry.removeBeanDefinition(beanName)
  }

  /**
    * 获取Bean
    *
    * @param requiredType
    * @tparam T
    * @return
    */
  def getBean[T](requiredType: Class[T]): T = {
    assertApplicationContext()
    applicationContext.getBean(requiredType)
  }

  /**
    * 获取Bean
    *
    * @param beanName
    * @tparam T
    * @return
    */
  def getBean[T](beanName: String): T = {
    assertApplicationContext()
    applicationContext.getBean(beanName).asInstanceOf[T]
  }

  private def assertApplicationContext(): Unit = {
    if (applicationContext == null) throw new RuntimeException("ApplicationContext属性为null")
  }
}