package com.equalexperts.scaladayscompetitionform

import com.uxforms.domain.{DataTransformationResult, FormData, FormDefinition, RequestInfo}
import com.uxforms.dsl.Form
import com.uxforms.submission.googlespreadsheet.{DateAwareGeneralConverter, GoogleSpreadsheetSubmission, SpreadsheetData}
import org.joda.time.Instant
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContext, Future}

object submitToGoogle {

  private val logger = LoggerFactory.getLogger(submitToGoogle.getClass)

  def convertFormData(form: Form, requestInfo: RequestInfo): SpreadsheetData = {

    new DateAwareGeneralConverter() {
      override def columnHeadings: (FormDefinition) => Seq[String] =
        super.columnHeadings(_) :+ "timestamp" :+ "xForwardedFor"

      override def columnValues: (FormData, FormDefinition, RequestInfo) => Seq[Seq[String]] =
        (data, formDef, rInfo) => super.columnValues(data, formDef, rInfo).map(_ :+ Instant.now().toString :+ rInfo.remoteAddress)

    }.convert(form, requestInfo)
  }

  def apply()(implicit classLoader: ClassLoader): GoogleSpreadsheetSubmission =
    new GoogleSpreadsheetSubmission(
      classLoader.getResourceAsStream("uxforms-service-account-key.json"),
      "SwanseaCon Entry Form",
      convertFormData
    ) {
      override def transform(form: Form, requestInfo: RequestInfo)(implicit ec: ExecutionContext): Future[DataTransformationResult] = {
        val res = super.transform(form, requestInfo)
        res.onFailure {
          case t: Throwable => logger.error("unable to submit to google", t)
        }
        res
      }
    }
}