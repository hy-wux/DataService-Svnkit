package com.service.framework.core.serializer

import com.service.framework.core.enums.SerializeProtocolEnum
import com.service.framework.core.exception.SerializerException

trait ServiceSerializer {

  /**
    * 序列化对象.
    *
    * @param obj 需要序更列化的对象
    * @return byte []
    */
  @throws[SerializerException]
  def serialize(obj: Any): Array[Byte]


  /**
    * 反序列化对象.
    *
    * @param param 需要反序列化的byte []
    * @param clazz java对象
    * @return 对象
    */
  @throws[SerializerException]
  def deserialize[T](param: Array[Byte], clazz: Class[T]): T

  def protocol: SerializeProtocolEnum.Value
}
