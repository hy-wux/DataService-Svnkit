package com.service.integrates.ini4j.service.impl

import java.io.File
import java.util

import com.service.integrates.ini4j.service.IniOperationService
import com.service.integrates.ini4j.tools.Ini4jTool
import com.service.integrates.ini4j.utils.Ini4jUtil
import org.ini4j.Ini
import org.springframework.stereotype.Service

@Service
class IniOperationServiceImpl extends IniOperationService {
  /**
    * 添加配置信息
    *
    * @param file    配置文件
    * @param section 节
    * @param key     键
    * @param value   值
    */
  override def addConfig(file: File, section: String, key: String, value: String): Unit = {
    // 读取配置文件
    var ini = new Ini(file)
    // 在节下添加配置
    ini = Ini4jUtil.addConfig(ini, section, key, value)
    // 将配置信息保存到配置文件中
    Ini4jTool.saveIniFile(ini, file)
  }

  /**
    * 添加配置信息
    *
    * @param file    配置文件
    * @param section 节
    * @param values  配置
    */
  override def addConfigs(file: File, section: String, values: util.Map[String, String]): Unit = {
    // 读取配置文件
    var ini = new Ini(file)
    // 在节下添加配置
    ini = Ini4jUtil.addConfigs(ini, section, values)
    // 将配置信息保存到配置文件中
    Ini4jTool.saveIniFile(ini, file)
  }

  /**
    * 删除配置信息
    *
    * @param file    配置文件
    * @param section 节
    * @param keys    配置
    */
  override def deleteConfigs(file: File, section: String, keys: Array[String]): Unit = {
    // 读取配置文件
    val ini = new Ini(file)
    keys.foreach(key => {
      ini.get(section).remove(key)
    })
    // 将配置信息保存到配置文件中
    Ini4jTool.saveIniFile(ini, file)
  }

  /**
    * 加载配置文件
    *
    * @param file 配置文件
    * @return
    */
  override def loadConfig(file: File): Ini = {
    new Ini(file)
  }

}