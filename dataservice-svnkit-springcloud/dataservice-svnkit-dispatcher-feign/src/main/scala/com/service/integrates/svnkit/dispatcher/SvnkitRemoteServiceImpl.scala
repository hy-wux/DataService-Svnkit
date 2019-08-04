package com.service.integrates.svnkit.dispatcher

import java.util

import com.service.framework.core.serializer.ServiceSerializer
import com.service.integrates.svnkit.api.remote.SvnkitRemoteService
import feign.codec.{Decoder, Encoder}
import feign.{Client, Contract, Feign}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.openfeign.{EnableFeignClients, FeignClientsConfiguration}
import org.springframework.context.annotation.Import
import org.springframework.stereotype.Service

import scala.collection.JavaConversions._

@EnableFeignClients
@Import(Array(classOf[FeignClientsConfiguration]))
@Service
class SvnkitRemoteServiceImpl extends SvnkitRemoteService {

  @Autowired val decoder: Decoder = null
  @Autowired val encoder: Encoder = null
  @Autowired val client: Client = null
  @Autowired val contract: Contract = null

  @Autowired
  val serviceSerializer: ServiceSerializer = null

  def getRemoteService(server: String): SvnkitRemoteProducer = {
    val producer = Feign.builder()
      .client(client)
      .encoder(encoder)
      .decoder(decoder)
      .contract(contract)
      .target(classOf[SvnkitRemoteProducer], s"http://${server.toUpperCase()}")
    producer
  }
  /**
    * 创建本地仓库
    *
    * @param server 服务器
    * @param repositoryName
    * @param enableRevisionProperties
    * @param force
    * @throws
    */
  override def createLocalRepository(server: String, repositoryName: String, enableRevisionProperties: Boolean, force: Boolean): Unit = {
    getRemoteService(server).createLocalRepository(repositoryName, enableRevisionProperties, force)
  }

  /**
    * 创建本地仓库并授权
    *
    * @param server 服务器
    * @param repositoryName
    * @param enableRevisionProperties
    * @param force
    * @param values
    * @throws
    */
  override def createLocalRepository(server: String, repositoryName: String, enableRevisionProperties: Boolean, force: Boolean, values: util.Map[String, String]): Unit = {
    getRemoteService(server).createLocalRepository(repositoryName, enableRevisionProperties, force, serviceSerializer.serialize(values))
  }

  /**
    * 批量创建本地仓库
    *
    * @param server 服务器
    * @param repositoryNames
    * @param enableRevisionProperties
    * @param force
    * @throws
    */
  override def createLocalRepositoryBatch(server: String, repositoryNames: util.List[String], enableRevisionProperties: Boolean, force: Boolean): Unit = {
    getRemoteService(server).createLocalRepositoryBatch(repositoryNames.toArray(Array[String]()), enableRevisionProperties, force)
  }

  /**
    * 批量创建本地仓库并授权
    *
    * @param server 服务器
    * @param repositoryNames
    * @param enableRevisionProperties
    * @param force
    * @param values
    * @throws
    */
  override def createLocalRepositoryBatch(server: String, repositoryNames: util.List[String], enableRevisionProperties: Boolean, force: Boolean, values: util.Map[String, String]): Unit = {
    getRemoteService(server).createLocalRepositoryBatch(repositoryNames.toArray(Array[String]()), enableRevisionProperties, force, serviceSerializer.serialize(values))
  }

  /**
    * 列出条目权限
    *
    * @param server         服务器
    * @param repositoryName 仓库名称
    * @param repositoryPath 条目路径
    * @return
    */
  override def listRights(server: String, repositoryName: String, repositoryPath: String): util.Map[String, util.Map[String, String]] = {
    getRemoteService(server).listRights(repositoryName, repositoryPath)
  }

  /**
    * 列出当前仓库所有用户
    *
    * @param server         服务器
    * @param repositoryName 仓库名称
    * @return Map(对象 -> 权限)
    */
  override def listRightUsers(server: String, repositoryName: String): util.Map[String, String] = {
    getRemoteService(server).listRightUsers(repositoryName)
  }

  /**
    * 仓库条目授权
    *
    * @param server         服务器
    * @param repositoryName 仓库名称
    * @param repositoryPath 条目路径
    * @param values         权限集
    */
  override def addRights(server: String, repositoryName: String, repositoryPath: String, values: util.Map[String, String]): Unit = {
    getRemoteService(server).addRights(repositoryName, repositoryPath, serviceSerializer.serialize(values))
  }

  /**
    * 删除对象权限
    *
    * @param server         服务器
    * @param repositoryName 仓库名称
    * @param repositoryPath 条目路径
    * @param names          对象名称
    * @return
    */
  override def deleteRights(server: String, repositoryName: String, repositoryPath: String, names: Array[String]): Unit = {
    getRemoteService(server).deleteRights(repositoryName, repositoryPath, names)
  }

  /**
    * 列出所有仓库
    *
    * @param server 服务器
    * @return
    */
  override def listRepositories(server: String): util.List[String] = {
    getRemoteService(server).listRepositories()
  }

  /**
    * 列出用户有权限的仓库
    *
    * @param server   服务器
    * @param username 用户名称
    * @return
    */
  override def userHaveRightRepositories(server: String, username: String): util.List[String] = {
    getRemoteService(server).userHaveRightRepositories(username)
  }

  /**
    * 列出组别有权限的仓库
    *
    * @param server    服务器
    * @param groupName 组别名称
    * @return
    */
  override def groupHaveRightRepositories(server: String, groupName: String): util.List[String] = {
    getRemoteService(server).groupHaveRightRepositories(groupName)
  }

  /**
    * 加载所有组信息
    *
    * @param server 服务器
    * @return
    */
  override def loadAllGroups(server: String): util.Map[String, Array[String]] = {
    getRemoteService(server).loadAllGroups()
  }

  /**
    * 列出所有组别
    *
    * @param server 服务器
    * @return
    */
  override def listGroups(server: String): util.List[String] = {
    getRemoteService(server).listGroups()
  }

  /**
    * 创建组别
    *
    * @param server 服务器
    * @param name   名称
    */
  override def createGroup(server: String, name: String): Unit = {
    getRemoteService(server).createGroup(name)
  }

  /**
    * 创建组别
    *
    * @param server 服务器
    * @param names  名称
    */
  override def createGroupBatch(server: String, names: util.List[String]): Unit = {
    getRemoteService(server).createGroupBatch(names.toArray(Array[String]()))
  }
  /**
    * 设置组别用户
    *
    * @param server 服务器
    * @param name   名称
    * @param users  用户
    */
  override def setGroupUsers(server: String, name: String, users: String): Unit = {
    getRemoteService(server).setGroupUsers(name, users)
  }

  /**
    * 列出所有用户
    *
    * @param server 服务器
    * @return
    */
  override def listUsers(server: String): util.List[String] = {
    getRemoteService(server).listUsers()
  }

  /**
    * 创建用户
    *
    * @param server   服务器
    * @param username 用户名称
    * @param password 用户密码
    */
  override def createUser(server: String, username: String, password: String): Unit = {
    getRemoteService(server).createUser(username, password)
  }

  /**
    * 批量创建用户
    *
    * @param server 服务器
    * @param users
    */
  override def createUserBatch(server: String, users: util.Map[String, String]): Unit = {
    getRemoteService(server).createUserBatch(serviceSerializer.serialize(users.toMap))
  }

  /**
    * 修改用户
    *
    * @param server   服务器
    * @param username 用户名称
    * @param password 用户密码
    */
  override def updateUser(server: String, username: String, password: String): Unit = {
    getRemoteService(server).updateUser(username, password)
  }
}
