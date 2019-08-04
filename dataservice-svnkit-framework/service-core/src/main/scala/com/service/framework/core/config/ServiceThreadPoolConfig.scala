package com.service.framework.core.config

import java.util.concurrent.{Executor, ThreadPoolExecutor}

import com.service.framework.core.config.properties.ServiceCoreProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@Configuration
class ServiceThreadPoolConfig {
  @Autowired
  val serviceCoreProperty: ServiceCoreProperties = null

  @Bean(name = Array("ServiceExecutor"))
  def ServiceExecutor: Executor = {
    val executor = new ThreadPoolTaskExecutor
    executor.setCorePoolSize(serviceCoreProperty.corePoolSize)
    executor.setMaxPoolSize(serviceCoreProperty.maxPoolSize)
    executor.setQueueCapacity(serviceCoreProperty.queueCapacity)
    executor.setKeepAliveSeconds(serviceCoreProperty.keepAliveSeconds)
    executor.setThreadNamePrefix(serviceCoreProperty.threadNamePrefix)
    // 线程池对拒绝任务的处理策略
    // 这里采用了CallerRunsPolicy策略，当线程池没有处理能力的时候，该策略会直接在 execute 方法的调用线程中运行被拒绝的任务
    // 如果执行程序已关闭，则会丢弃该任务
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy)
    executor.initialize()
    executor
  }
}
