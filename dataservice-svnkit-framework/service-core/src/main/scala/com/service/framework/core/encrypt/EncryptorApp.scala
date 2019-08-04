package com.service.framework.core.encrypt

import com.ulisesbocchio.jasyptspringboot.encryptor.DefaultLazyEncryptor
import org.springframework.core.env.StandardEnvironment

object EncryptorApp extends App {
  if (args.length == 2) {
    System.setProperty("jasypt.encryptor.password", args(0))
    val encryptor = new DefaultLazyEncryptor(new StandardEnvironment)
    if (args(1).startsWith("ENC(")) {
      // 解密
      println(encryptor.decrypt(args(1).substring(4, args(1).length - 1)))
    } else {
      // 加密
      println(s"ENC(${encryptor.encrypt(args(1))})")
    }
  } else {
    println("Three parameters must be specified: [jasypt.encryptor.password] [string]")
  }
}
