package com.service.integrates.svnkit.provider.service.subversionhttp

import java.io.{File, FileInputStream, FileOutputStream}
import java.util

import com.service.integrates.svnkit.api.service.SvnkitUserOperationService
import org.apache.commons.codec.digest.Md5Crypt
import org.apache.commons.io.IOUtils
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

import scala.collection.JavaConversions._

@Service
@ConditionalOnProperty(value = Array("service.svnkit.version"), havingValue = "subversionhttp")
class SvnkitUserServiceImpl extends SvnkitUserOperationService {

  def userConfigFile(): File = new File(property.getPath, property.getPasswdFile)

  def userConfigTempFile(): File = new File(property.getPath, s"${property.getPasswdFile}.wux")

  def loadUserConfig(): util.List[String] = {
    val input = new FileInputStream(userConfigFile())
    val lines = IOUtils.readLines(input, "UTF-8")
    input.close()
    lines
  }

  def saveUsers(lines: util.List[String]): Unit = {
    val source = new FileOutputStream(userConfigTempFile())
    IOUtils.writeLines(lines, "\r\n", source, "UTF-8")
    source.close()

    val input = new FileInputStream(userConfigTempFile())
    val output = new FileOutputStream(userConfigFile())
    IOUtils.copy(input, output)
    input.close()
    output.close()

    userConfigTempFile().delete()
  }

  /**
    * 列出所有用户
    *
    * @return
    */
  override def listUsers(): util.List[String] = {
    loadUserConfig.map(_.split(":")(0)).toList
  }

  /**
    * 创建用户
    *
    * @param username 用户名称
    * @param password 用户密码
    */
  @throws[Exception]
  override def createUser(username: String, password: String): Unit = {
    val lines = loadUserConfig()
    if (lines.filter(_.split(":", -1)(0).trim.equals(s"${username}")).size < 1) {
      lines.add(s"${username}:${Md5Crypt.apr1Crypt(password)}")
      saveUsers(lines)
    } else {
      throw new Exception("用户已存在")
    }
  }

  /**
    * 批量创建用户
    *
    * @param users
    */
  override def createUserBatch(users: util.Map[String, String]): Unit = {
    val lines = loadUserConfig()
    users.foreach(user => {
      if (lines.filter(_.split(":", -1)(0).trim.equals(s"${user._1}")).size < 1) {
        // 添加用户
        lines.add(s"${user._1}:${Md5Crypt.apr1Crypt(user._2)}")
      }
    })
    saveUsers(lines)
  }

  /**
    * 修改用户
    *
    * @param username 用户名称
    * @param password 用户密码
    */
  @throws[Exception]
  override def updateUser(username: String, password: String): Unit = {
    val lines = loadUserConfig().map(line => if (line.split(":", -1)(0).trim.equals(s"${username}")) s"${username}:${Md5Crypt.apr1Crypt(password)}" else line)
    saveUsers(lines)
  }
}
