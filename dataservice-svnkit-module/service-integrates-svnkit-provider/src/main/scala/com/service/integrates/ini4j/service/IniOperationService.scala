package com.service.integrates.ini4j.service

import java.io.File
import java.util

import org.ini4j.Ini

trait IniOperationService {
  /**
    * 添加配置信息
    *
    * @param file    配置文件
    * @param section 节
    * @param key     键
    * @param value   值
    */
  def addConfig(file: File, section: String, key: String, value: String): Unit

  /**
    * 添加配置信息
    *
    * @param file    配置文件
    * @param section 节
    * @param values  配置
    */
  def addConfigs(file: File, section: String, values: util.Map[String, String]): Unit

  /**
    * 删除配置信息
    *
    * @param file    配置文件
    * @param section 节
    * @param keys    配置
    */
  def deleteConfigs(file: File, section: String, keys: Array[String]): Unit

  /**
    * 加载配置文件
    *
    * @param file 配置文件
    * @return
    */
  def loadConfig(file: File): Ini
}
