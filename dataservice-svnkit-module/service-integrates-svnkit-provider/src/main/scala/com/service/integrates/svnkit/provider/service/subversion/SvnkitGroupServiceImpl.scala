package com.service.integrates.svnkit.provider.service.subversion

import java.io.File
import java.util

import com.service.framework.core.utils.CommUtil
import com.service.integrates.ini4j.service.IniOperationService
import com.service.integrates.svnkit.api.service.SvnkitGroupOperationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

import scala.collection.JavaConversions._

@Service
@ConditionalOnProperty(value = Array("service.svnkit.version"), havingValue = "subversion")
class SvnkitGroupServiceImpl extends SvnkitGroupOperationService {

  @Autowired
  val iniService: IniOperationService = null

  val sectionName = "groups"

  def groupConfigFile(): File = new File(property.getPath, property.getGroupFile)

  /**
    * 列出所有组别
    *
    * @return
    */
  override def listGroups(): util.List[String] = {
    iniService.loadConfig(groupConfigFile()).get(sectionName).keySet().toList
  }

  /**
    * 加载所有组信息
    *
    * @return
    */
  override def loadAllGroups(): util.Map[String, Array[String]] = {
    val section = iniService.loadConfig(groupConfigFile()).get(sectionName)
    if (CommUtil.isNotEmpty(section)) {
      section.keys.map(key => Map(key -> section.get(key).split(",", -1))).fold(Map.empty[String, Array[String]])(_ ++ _)
    } else {
      Map.empty[String, Array[String]]
    }
  }

  /**
    * 创建组别
    *
    * @param name 名称
    */
  @throws[Exception]
  override def createGroup(name: String): Unit = {
    val ini = iniService.loadConfig(groupConfigFile())
    if (ini.keys.contains(sectionName)) {
      val section = ini.get(sectionName)
      if (section.keys.contains(name)) {
        throw new Exception("组别已存在")
      } else {
        iniService.addConfig(groupConfigFile(), sectionName, name, "")
      }
    } else {
      iniService.addConfig(groupConfigFile(), sectionName, name, "")
    }
  }

  /**
    * 批量创建组别
    *
    * @param names 名称
    */
  override def createGroupBatch(names: util.List[String]): Unit = {
    val ini = iniService.loadConfig(groupConfigFile())
    if (ini.keys.contains(sectionName)) {
      iniService.addConfigs(groupConfigFile(), sectionName, names.filter(!ini.get(sectionName).contains(_)).map(name => Map(name -> "")).fold(Map.empty[String, String])(_ ++ _))
    } else {
      iniService.addConfigs(groupConfigFile(), sectionName, names.map(name => Map(name -> "")).fold(Map.empty[String, String])(_ ++ _))
    }
  }

  /**
    * 设置组别用户
    *
    * @param name  名称
    * @param users 用户
    */
  override def setGroupUsers(name: String, users: String): Unit = {
    iniService.addConfig(groupConfigFile(), sectionName, name, users)
  }
}
