package com.service.framework.web.utils

import java.io.{File, IOException}

import com.service.framework.core.component.{IdWorker, SpringContextHolder}
import com.service.framework.web.ServiceWebConfigurationProperties
import org.apache.tomcat.util.http.fileupload.FileUploadBase
import org.apache.tomcat.util.http.fileupload.FileUploadBase.FileSizeLimitExceededException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.multipart.MultipartFile

import scala.beans.BeanProperty

class FileUploadUtils {

}

object FileUploadUtils {
  @Autowired
  val properties: ServiceWebConfigurationProperties = SpringContextHolder.getBean(classOf[ServiceWebConfigurationProperties])

  @Autowired
  val idWorker: IdWorker = SpringContextHolder.getBean(classOf[IdWorker])

  /**
    * 默认大小 50M
    */
  val DEFAULT_MAX_SIZE = 52428800

  /**
    * 默认上传的地址
    */
  @BeanProperty
  var defaultBaseDir = properties.getProfile

  /**
    * 默认的文件名最大长度
    */
  val DEFAULT_FILE_NAME_LENGTH = 200

  /**
    * 默认文件类型jpg
    */
  val IMAGE_JPG_EXTENSION = ".jpg"

  /**
    * 默认的头像地址
    *
    * @return
    */
  def getDefaultAvatarDir: String = new File(getDefaultBaseDir, "avatar").getAbsolutePath

  /**
    * 默认的上传地址
    *
    * @return
    */
  def getDefaultUploadDir: String = new File(getDefaultBaseDir, "upload").getAbsolutePath

  /**
    * 默认的下载地址
    *
    * @return
    */
  def getDefaultDownloadDir: String = new File(getDefaultBaseDir, "download").getAbsolutePath

  /**
    * 以默认配置进行文件上传
    *
    * @param file 上传的文件
    * @return 文件名称
    * @throws Exception
    */
  @throws[IOException]
  def uploadAvatar(file: MultipartFile): String = {
    try {
      upload(getDefaultAvatarDir, file, FileUploadUtils.IMAGE_JPG_EXTENSION)
    } catch {
      case e: Exception =>
        throw new IOException(e)
    }
  }

  /**
    * 根据文件路径上传
    *
    * @param baseDir 相对应用的基目录
    * @param file    上传的文件
    * @return 文件名称
    * @throws IOException
    */
  @throws[IOException]
  def upload(baseDir: String, file: MultipartFile): String = {
    try {
      upload(baseDir, file, file.getOriginalFilename.substring(file.getOriginalFilename.lastIndexOf(".")))
    } catch {
      case e: Exception =>
        throw new IOException(e)
    }
  }

  /**
    * 文件上传
    *
    * @param baseDir
    * @param file
    * @param extension
    * @throws FileSizeLimitExceededException
    * @throws Exception
    * @return
    */
  @throws[FileSizeLimitExceededException]
  @throws[Exception]
  def upload(baseDir: String, file: MultipartFile, extension: String): String = {
    val fileNameLength = file.getOriginalFilename.length
    if (fileNameLength > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH) throw new Exception("文件名称太长了")
    assertAllowed(file)
    val fileName = s"${idWorker.nextId()}${extension}"
    val desc = getAbsoluteFile(baseDir, new File(baseDir, fileName))
    file.transferTo(desc)
    fileName
  }

  @throws[IOException]
  private def getAbsoluteFile(uploadDir: String, file: File) = {
    if (!file.getParentFile.exists) file.getParentFile.mkdirs
    if (!file.exists) file.createNewFile
    file
  }

  /**
    * 文件大小校验
    *
    * @param file 上传的文件
    * @return
    * @throws FileSizeLimitExceededException 如果超出最大大小
    */
  @throws[FileSizeLimitExceededException]
  def assertAllowed(file: MultipartFile): Unit = {
    val size = file.getSize
    if (DEFAULT_MAX_SIZE != -1 && size > DEFAULT_MAX_SIZE) {
      throw new FileUploadBase.FileSizeLimitExceededException("not allowed upload upload", size, DEFAULT_MAX_SIZE)
    }
  }
}
