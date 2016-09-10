package com.equalexperts.scaladayscompetitionform

import com.uxforms.domain.{FormData, FormDefinition, RequestInfo}
import com.uxforms.dsl.Form
import com.uxforms.submission.googlespreadsheet.{DateAwareGeneralConverter, GoogleSpreadsheetSubmission, SpreadsheetData}
import org.joda.time.Instant

object submitToGoogle {

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
    )
}