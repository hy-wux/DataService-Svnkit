package com.service.visual.web.admin.config

import java.util

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect
import com.service.visual.web.admin.filter.KaptchaValidateFilter
import com.service.visual.web.admin.core.shiro.realm.UserRealm
import javax.servlet.Filter
import org.apache.shiro.cache.CacheManager
import org.apache.shiro.cache.ehcache.EhCacheManager
import org.apache.shiro.codec.Base64
import org.apache.shiro.spring.LifecycleBeanPostProcessor
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor
import org.apache.shiro.spring.web.ShiroFilterFactoryBean
import org.apache.shiro.spring.web.config.{DefaultShiroFilterChainDefinition, ShiroFilterChainDefinition}
import org.apache.shiro.web.mgt.{CookieRememberMeManager, DefaultWebSecurityManager}
import org.apache.shiro.web.servlet.SimpleCookie
import org.springframework.beans.factory.config.MethodInvokingFactoryBean
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.core.io.ClassPathResource

@Configuration
class ShiroConfig {
  @Bean
  def userRealm = new UserRealm

  /**
    * 安全管理器
    */
  @Bean
  def defaultWebSecurityManager(rememberMeManager: CookieRememberMeManager, cacheShiroManager: CacheManager): DefaultWebSecurityManager = {
    val securityManager = new DefaultWebSecurityManager
    securityManager.setRealm(userRealm)
    securityManager.setCacheManager(cacheShiroManager)
    securityManager.setRememberMeManager(rememberMeManager)
    securityManager
  }

  /**
    * 记住密码Manager
    */
  @Bean
  def cookieRememberMeManager(rememberMeCookie: SimpleCookie): CookieRememberMeManager = {
    val manager = new CookieRememberMeManager
    manager.setCipherKey(Base64.decode("Z3VucwAAAAAAAAAAAAAAAA=="))
    manager.setCookie(rememberMeCookie)
    manager
  }

  /**
    * 记住密码Cookie
    */
  @Bean
  def rememberMeCookie: SimpleCookie = {
    val simpleCookie = new SimpleCookie("rememberMe")
    simpleCookie.setHttpOnly(true)
    simpleCookie.setMaxAge(7 * 24 * 60 * 60) //7天

    simpleCookie
  }

  /**
    * 集成Ehcache
    *
    * @return
    */
  @Bean
  def ehCacheManagerFactoryBean: EhCacheManagerFactoryBean = {
    val cacheManagerFactoryBean = new EhCacheManagerFactoryBean
    cacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"))
    cacheManagerFactoryBean.setShared(true)
    cacheManagerFactoryBean
  }

  /**
    * 缓存管理器 使用Ehcache实现
    */
  @Bean
  def shiroCacheManager(ehcache: EhCacheManagerFactoryBean): CacheManager = {
    val ehCacheManager = new EhCacheManager
    ehCacheManager.setCacheManager(ehcache.getObject)
    ehCacheManager
  }

  @Bean
  def kaptchaValidateFilter(): KaptchaValidateFilter = new KaptchaValidateFilter

  /**
    * Shiro的过滤器链
    */
  @Bean
  def shiroFilterFactoryBean(securityManager: DefaultWebSecurityManager): ShiroFilterFactoryBean = {
    val shiroFilter = new ShiroFilterFactoryBean
    shiroFilter.setSecurityManager(securityManager)

    shiroFilter.setLoginUrl("/login")
    shiroFilter.setSuccessUrl("/")
    shiroFilter.setUnauthorizedUrl("/global/error")

    val filters = new util.LinkedHashMap[String, Filter]
    filters.put("kaptchaValidate", kaptchaValidateFilter)
    shiroFilter.setFilters(filters)

    /*
      * 配置shiro拦截器链
      *
      * anon  不需要认证
      * authc 需要认证
      * user  验证通过或RememberMe登录的都可以
      *
      * 当应用开启了rememberMe时，用户下次访问时可以是一个user，但不会是authc，因为authc是需要重新认证的
      *
      * 顺序从上到下，优先级依次降低
      *
      */
    val filterChainDefinitionMap = new util.LinkedHashMap[String, String]
    filterChainDefinitionMap.put("/css/**", "anon")
    filterChainDefinitionMap.put("/fonts/**", "anon")
    filterChainDefinitionMap.put("/img/**", "anon")
    filterChainDefinitionMap.put("/js/**", "anon")
    filterChainDefinitionMap.put("/libs/**", "anon")
    filterChainDefinitionMap.put("/login", "anon, kaptchaValidate")
    filterChainDefinitionMap.put("/kaptcha", "anon")

    filterChainDefinitionMap.put("/stress/**", "anon") // 压力测试不需要认证

    filterChainDefinitionMap.put("/swagger-ui.html", "anon") // 接口测试不需要认证
    filterChainDefinitionMap.put("/swagger-resources/**", "anon") // 接口测试不需要认证
    filterChainDefinitionMap.put("/webjars/**", "anon") // 接口测试不需要认证
    filterChainDefinitionMap.put("/v2/api-docs/**", "anon") // 接口测试不需要认证

    filterChainDefinitionMap.put("/api/**", "anon") // 接口不需要认证

    filterChainDefinitionMap.put("/**", "user")
    shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMap)

    shiroFilter
  }

  @Bean
  def shiroFilterChainDefinition: ShiroFilterChainDefinition = {
    // 不需要在此处配置权限页面，因为ShiroFilterFactoryBean已经配置过，但是此处必须存在
    // 因为shiro-spring-boot-web-starter或查找此Bean，没有会报错
    new DefaultShiroFilterChainDefinition
  }

  /**
    * 在方法中 注入 securityManager,进行代理控制
    */
  @Bean
  def methodInvokingFactoryBean(securityManager: DefaultWebSecurityManager): MethodInvokingFactoryBean = {
    val bean = new MethodInvokingFactoryBean
    bean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager")
    bean.setArguments(Array[AnyRef](securityManager))
    bean
  }

  /**
    * Shiro生命周期处理器:
    * 用于在实现了Initializable接口的Shiro bean初始化时调用Initializable接口回调(例如:UserRealm)
    * 在实现了Destroyable接口的Shiro bean销毁时调用 Destroyable接口回调(例如:DefaultSecurityManager)
    */
  @Bean
  def lifecycleBeanPostProcessor = new LifecycleBeanPostProcessor

  /**
    * thymeleaf模板引擎和shiro框架的整合
    */
  @Bean
  def shiroDialect = new ShiroDialect

  /**
    * 启用shrio授权注解拦截方式，AOP式方法级权限检查
    */
  @Bean
  def authorizationAttributeSourceAdvisor(securityManager: DefaultWebSecurityManager): AuthorizationAttributeSourceAdvisor = {
    val authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor
    authorizationAttributeSourceAdvisor.setSecurityManager(securityManager)
    authorizationAttributeSourceAdvisor
  }
}
