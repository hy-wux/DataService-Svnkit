package com.service.framework.jdbc.config.properties

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ServiceJdbcExportProperties {
  @Value("${service.jdbc.export.format.date-pattern:\"yyyy-MM-dd\"}")
  var datePattern: String = null

  @Value("${service.jdbc.export.format.time-pattern:\"hh:mm:ss\"}")
  var timePattern: String = null

  @Value("${service.jdbc.export.format.datetime-pattern:\"yyyy-MM-dd hh:mm:ss\"}")
  var timestampPattern: String = null

  @Value("${service.jdbc.export.with-chk:false}")
  val withChk: String = null
}
