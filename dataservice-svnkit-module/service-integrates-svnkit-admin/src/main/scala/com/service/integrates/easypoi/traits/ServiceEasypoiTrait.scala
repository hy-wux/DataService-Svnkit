package com.service.integrates.easypoi.traits

import java.io.{File, FileOutputStream}
import java.util

import cn.afterturn.easypoi.excel.entity.{ExportParams, ImportParams}
import cn.afterturn.easypoi.excel.{ExcelExportUtil, ExcelImportUtil}
import com.baomidou.mybatisplus.mapper.Wrapper
import com.baomidou.mybatisplus.service.IService
import com.service.framework.core.component.IdWorker
import com.service.framework.web.utils.FileUploadUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.multipart.MultipartFile

import scala.collection.JavaConversions._

trait ServiceEasypoiTrait {

  @Autowired val idWorker: IdWorker = null

  /**
    * 生成模板文件
    *
    * @param className
    * @param templateName
    * @return
    */
  def generateTemplate[T](className: Class[T], templateName: String): String = {
    val params = new ExportParams()
    params.setHeight(6)

    val workbook = ExcelExportUtil.exportExcel(params, className, List())
    val filename = s"${templateName}_模板.xls"
    val os = new FileOutputStream(new File(FileUploadUtils.getDefaultDownloadDir, filename))

    workbook.write(os)
    os.flush()
    os.close()

    filename
  }

  /**
    * 导入记录
    *
    * @param file
    * @param className
    * @tparam T
    * @return
    */
  def importRecords[T](file: MultipartFile, className: Class[T]): util.List[T] = {
    val filename = FileUploadUtils.upload(FileUploadUtils.getDefaultUploadDir, file)
    ExcelImportUtil.importExcel(new File(FileUploadUtils.getDefaultUploadDir, filename), className, new ImportParams())
  }

  /**
    * 导出记录
    *
    * @param service
    * @param wrapper
    * @param className
    * @param titleName
    * @tparam T
    * @return
    */
  def exportRecords[T](service: IService[T], wrapper: Wrapper[T], className: Class[T], titleName: String): String = {
    val records = service.selectList(wrapper)
    exportRecords(records, className, titleName)
  }

  /**
    * 导出记录
    *
    * @param titleName
    * @param className
    * @param records
    * @tparam T
    * @return
    */
  def exportRecords[T](records: util.List[T], className: Class[T], titleName: String): String = {
    val params = new ExportParams()
    params.setTitle(titleName)
    params.setHeight(6)
    params.setTitleHeight(6)

    val workbook = ExcelExportUtil.exportExcel(params, className, records)
    val filename = s"${titleName}_${idWorker.nextId()}.xls"
    val os = new FileOutputStream(new File(FileUploadUtils.getDefaultDownloadDir, filename))
    workbook.write(os)
    os.flush()
    os.close()

    filename
  }
}
