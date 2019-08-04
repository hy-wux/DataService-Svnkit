package com.service.framework.web.controller

import java.io.{File, FileInputStream, UnsupportedEncodingException}
import java.net.URLEncoder

import com.service.framework.web.utils.FileUploadUtils
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.apache.commons.io.IOUtils
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping}

@Controller
@RequestMapping(Array("/common"))
class CommonController {

  @throws[UnsupportedEncodingException]
  def setFileDownloadHeader(request: HttpServletRequest, fileName: String): String = {
    val agent = request.getHeader("USER-AGENT")
    var filename = fileName
    if (agent.contains("MSIE")) { // IE浏览器
      filename = URLEncoder.encode(filename, "utf-8")
      filename = filename.replace("+", " ")
    } else if (agent.contains("Firefox")) { // 火狐浏览器
      filename = new String(fileName.getBytes, "ISO8859-1")
    } else if (agent.contains("Chrome")) { // google浏览器
      filename = URLEncoder.encode(filename, "utf-8")
    } else { // 其它浏览器
      filename = URLEncoder.encode(filename, "utf-8")
    }
    filename
  }

  @GetMapping(Array("/download"))
  def fileDownload(fileName: String, delete: Boolean, response: HttpServletResponse, request: HttpServletRequest): Unit = {
    try {
      val filePath = new File(FileUploadUtils.getDefaultDownloadDir, fileName)
      response.setCharacterEncoding("utf-8")
      response.setContentType("multipart/form-data")
      response.setHeader("Content-Disposition", "attachment;fileName=" + setFileDownloadHeader(request, fileName))
      val input = new FileInputStream(filePath)
      IOUtils.copy(input, response.getOutputStream)
      input.close()
      if (delete) {
        filePath.delete()
      }
    } catch {
      case _: Throwable =>
    }
  }
}
