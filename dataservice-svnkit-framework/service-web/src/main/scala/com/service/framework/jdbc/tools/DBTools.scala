package com.service.framework.jdbc.tools

import java.sql.{ResultSet, Types}
import java.text.{DecimalFormat, SimpleDateFormat}

import com.service.framework.core.component.SpringContextHolder
import com.service.framework.jdbc.config.properties.ServiceJdbcExportProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.support.rowset.SqlRowSet

/**
  * @author 伍鲜
  *
  *         数据库工具类
  */
class DBTools {

}

/**
  * @author 伍鲜
  *
  *         数据库工具类
  */
object DBTools {

  @Autowired
  val serviceJdbcExportProperty: ServiceJdbcExportProperties = null

  val dateFormat = new SimpleDateFormat(serviceJdbcExportProperty.datePattern)
  val timeFormat = new SimpleDateFormat(serviceJdbcExportProperty.timePattern)
  val timestampFormat = new SimpleDateFormat(serviceJdbcExportProperty.timestampPattern)

  val df0 = new DecimalFormat("0")
  val df1 = new DecimalFormat("0.0")
  val df2 = new DecimalFormat("0.00")
  val df3 = new DecimalFormat("0.000")
  val df4 = new DecimalFormat("0.0000")
  val df5 = new DecimalFormat("0.00000")
  val df6 = new DecimalFormat("0.000000")
  val df7 = new DecimalFormat("0.0000000")
  val df8 = new DecimalFormat("0.00000000")
  val df9 = new DecimalFormat("0.000000000")
  val dfA = new DecimalFormat("0.0000000000")

  val df = new Array[DecimalFormat](11)
  df(0) = df0
  df(1) = df1
  df(2) = df2
  df(3) = df3
  df(4) = df4
  df(5) = df5
  df(6) = df6
  df(7) = df7
  df(8) = df8
  df(9) = df9
  df(10) = dfA

  implicit class SQLRowSetImplicit(val resultSet: SqlRowSet) {

    /**
      * 获取条记录的数据
      *
      * @return
      */
    def getLineData(): List[Any] = {
      val metadata = resultSet.getMetaData
      val columnCount = metadata.getColumnCount

      (1 to columnCount).map(i => {
        if (resultSet.getObject(i) == null) {
          ""
        } else {
          metadata.getColumnType(i) match {
            case Types.BIT =>
              resultSet.getObject(i)
            case Types.TINYINT =>
              resultSet.getObject(i)
            case Types.SMALLINT =>
              resultSet.getObject(i)
            case Types.INTEGER =>
              resultSet.getObject(i)
            case Types.BIGINT =>
              resultSet.getObject(i)

            case Types.FLOAT =>
              resultSet.getObject(i)
            case Types.DOUBLE =>
              resultSet.getObject(i)

            case Types.REAL =>
              resultSet.getObject(i)
            case Types.NUMERIC =>
              resultSet.getObject(i)
            case Types.DECIMAL =>
              resultSet.getObject(i)

            case Types.CHAR =>
              resultSet.getObject(i)
            case Types.VARCHAR =>
              resultSet.getObject(i)
            case Types.LONGVARCHAR =>
              resultSet.getObject(i)

            case Types.DATE =>
              dateFormat.format(resultSet.getDate(i))
            case Types.TIME =>
              timeFormat.format(resultSet.getTime(i))
            case Types.TIMESTAMP =>
              timestampFormat.format(resultSet.getTimestamp(i))

            case Types.CLOB =>
              ""
            case Types.BLOB =>
              ""
            case Types.NULL =>
              ""
            case _ =>
              ""
          }
        }
      }).toList
    }

    def getHeader(): List[String] = {
      val meta = resultSet.getMetaData()
      val count = meta.getColumnCount
      (1 to count).map(i => {
        meta.getColumnName(i)
      }).toList
    }

  }


  implicit class ResultSetImplicit(val resultSet: ResultSet) {

    resultSet.setFetchSize(5000)

    /**
      * 获取条记录的数据
      *
      * @return
      */
    def getLineData(): List[Any] = {
      val metadata = resultSet.getMetaData
      val columnCount = metadata.getColumnCount

      (1 to columnCount).map(i => {
        if (resultSet.getObject(i) == null) {
          ""
        } else {
          metadata.getColumnType(i) match {
            case Types.BIT =>
              resultSet.getObject(i)
            case Types.TINYINT =>
              resultSet.getObject(i)
            case Types.SMALLINT =>
              resultSet.getObject(i)
            case Types.INTEGER =>
              resultSet.getObject(i)
            case Types.BIGINT =>
              resultSet.getObject(i)

            case Types.FLOAT =>
              resultSet.getObject(i)
            case Types.DOUBLE =>
              resultSet.getObject(i)

            case Types.REAL =>
              resultSet.getObject(i)
            case Types.NUMERIC =>
              resultSet.getObject(i)
            case Types.DECIMAL =>
              resultSet.getObject(i)

            case Types.CHAR =>
              resultSet.getObject(i)
            case Types.VARCHAR =>
              resultSet.getObject(i)
            case Types.LONGVARCHAR =>
              resultSet.getObject(i)

            case Types.DATE =>
              dateFormat.format(resultSet.getDate(i))
            case Types.TIME =>
              timeFormat.format(resultSet.getTime(i))
            case Types.TIMESTAMP =>
              timestampFormat.format(resultSet.getTimestamp(i))

            case Types.CLOB =>
              val clob = resultSet.getClob(i)
              clob.getSubString(1, clob.length().toInt)
            case Types.BLOB =>
              ""
            case Types.NULL =>
              ""
            case _ =>
              ""
          }
        }
      }).toList
    }

    def getHeader(): List[String] = {
      val meta = resultSet.getMetaData()
      val count = meta.getColumnCount
      (1 to count).map(i => {
        meta.getColumnName(i)
      }).toList
    }
  }

}