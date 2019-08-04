package com.service.integrates.svnkit.admin.swagger

import java.util

import com.service.integrates.svnkit.api.remote.SvnkitRemoteService
import io.swagger.annotations.{Api, ApiOperation}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{PostMapping, RequestMapping, RestController}

@RestController
@RequestMapping(value = Array("/api/svnkit"))
@Api("Svnkit资源权限管理功能测试")
class ServiceSvnkitApi {

  @Autowired val svnkitRemoteService: SvnkitRemoteService = null

  @ApiOperation("列出所有仓库")
  @PostMapping(value = Array("/listRepositories"))
  def listRepositories(server: String): util.List[String] = {
    svnkitRemoteService.listRepositories(server)
  }

  @ApiOperation("列出所有组别")
  @PostMapping(value = Array("/listGroups"))
  def listGroups(server: String): util.List[String] = {
    svnkitRemoteService.listGroups(server)
  }

  @ApiOperation("列出所有用户")
  @PostMapping(value = Array("/listUsers"))
  def listUsers(server: String): util.List[String] = {
    svnkitRemoteService.listUsers(server)
  }
}
