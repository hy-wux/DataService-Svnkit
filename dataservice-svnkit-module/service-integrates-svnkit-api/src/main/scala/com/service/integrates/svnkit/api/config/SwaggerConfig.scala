package com.service.integrates.svnkit.api.config

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
class SwaggerConfig {

  @Bean
  @ConditionalOnMissingBean(value = Array(classOf[Docket]))
  def createRestApi: Docket = new Docket(DocumentationType.SWAGGER_2)
    .apiInfo(apiInfo)
    .select
    .apis(RequestHandlerSelectors.withClassAnnotation(classOf[Api]))
    .build

  private def apiInfo: ApiInfo = new ApiInfoBuilder()
    .title("SVN资源权限管理系统 - 接口测试")
    .description("用于 - SVN资源权限管理系统 - 接口测试")
    .contact(new Contact("伍鲜", "", "hy_wux@outlook.com"))
    .version("1.0")
    .build
}
