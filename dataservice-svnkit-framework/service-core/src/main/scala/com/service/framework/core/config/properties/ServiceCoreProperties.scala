package com.service.framework.core.config.properties

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ServiceCoreProperties {
  /**
    * 核心线程数：线程池创建时候初始化的线程数
    */
  @Value("${service.core.executor.pool.core-size:100}")
  val corePoolSize = 100

  /**
    * 最大线程数：线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
    */
  @Value("${service.core.executor.pool.max-size:1000}")
  val maxPoolSize = 1000

  /**
    * 缓冲队列200：用来缓冲执行任务的队列
    */
  @Value("${service.core.executor.pool.queue-capacity:100}")
  val queueCapacity = 100

  /**
    * 允许线程的空闲时间(单位：秒)：当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
    */
  @Value("${service.core.executor.pool.keep-alive-seconds:60}")
  val keepAliveSeconds = 60

  /**
    * 线程池名的前缀：设置好了之后可以方便我们定位处理任务所在的线程池
    */
  @Value("${service.core.executor.pool.thread-name-prefix:Service-Executor-}")
  val threadNamePrefix = "Service-Executor-"

}
