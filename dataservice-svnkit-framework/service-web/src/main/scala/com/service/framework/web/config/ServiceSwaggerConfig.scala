package com.service.framework.web.config

import io.swagger.annotations.Api
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.{Bean, Configuration}
import springfox.documentation.builders.{ApiInfoBuilder, RequestHandlerSelectors}
import springfox.documentation.service.{ApiInfo, Contact}
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class ServiceSwaggerConfig {

  @Bean
  @ConditionalOnMissingBean(value = Array(classOf[Docket]))
  def createRestApi: Docket = new Docket(DocumentationType.SWAGGER_2)
    .apiInfo(apiInfo)
    .select
    .apis(RequestHandlerSelectors.withClassAnnotation(classOf[Api]))
    .build

  private def apiInfo: ApiInfo = new ApiInfoBuilder()
    .title(s"接口测试")
    .description(s"接口测试")
    .contact(new Contact("伍鲜", "", "hy_wux@outlook.com"))
    .version("1.0")
    .build
}
