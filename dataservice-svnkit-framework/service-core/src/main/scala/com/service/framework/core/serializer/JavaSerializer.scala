package com.service.framework.core.serializer

import java.io._

import com.service.framework.core.enums.SerializeProtocolEnum
import com.service.framework.core.exception.SerializerException

class JavaSerializer extends ServiceSerializer {
  /**
    * 序列化对象.
    *
    * @param obj 需要序更列化的对象
    * @return byte []
    */
  @throws[SerializerException]
  override def serialize(obj: Any): Array[Byte] = {
    val arrayOutputStream = new ByteArrayOutputStream
    val objectOutput = new ObjectOutputStream(arrayOutputStream)
    try {
      objectOutput.writeObject(obj)
      objectOutput.flush()
      return arrayOutputStream.toByteArray
    } catch {
      case e: IOException => throw new SerializerException("java serialize error " + e.getMessage)
    } finally {
      if (arrayOutputStream != null) arrayOutputStream.close()
      if (objectOutput != null) objectOutput.close()
    }
  }

  /**
    * 反序列化对象.
    *
    * @param param 需要反序列化的byte []
    * @param clazz java对象
    * @return 对象
    */
  @throws[SerializerException]
  override def deserialize[T](param: Array[Byte], clazz: Class[T]): T = {
    val arrayInputStream = new ByteArrayInputStream(param)
    val input = new ObjectInputStream(arrayInputStream)
    try {
      return input.readObject.asInstanceOf[T]
    } catch {
      case e@(_: IOException | _: ClassNotFoundException) => throw new SerializerException("java deSerialize error " + e.getMessage)
    } finally {
      if (arrayInputStream != null) arrayInputStream.close()
      if (input != null) input.close()
    }
  }

  override def protocol: SerializeProtocolEnum.Value = SerializeProtocolEnum.JDK
}