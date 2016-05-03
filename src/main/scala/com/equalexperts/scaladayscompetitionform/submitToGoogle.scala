package com.equalexperts.scaladayscompetitionform

import com.uxforms.domain.{FormData, FormDefinition, RequestInfo, Submission}
import com.uxforms.submission.googlespreadsheet.{DateAwareGeneralConverter, GoogleSpreadsheetSubmission, SpreadsheetData}
import org.joda.time.Instant

object submitToGoogle {

  def convertFormData(data: FormData, formDef: FormDefinition, requestInfo: RequestInfo): SpreadsheetData = {

    new DateAwareGeneralConverter() {
      override def columnHeadings: (FormDefinition) => Seq[String] =
        super.columnHeadings(_) :+ "timestamp"

      override def columnValues: (FormData, FormDefinition, RequestInfo) => Seq[Seq[String]] =
        super.columnValues(_, _, _).map(_ :+ Instant.now().toString)

    }.convert(data, formDef, requestInfo)
  }

  def apply()(implicit classLoader: ClassLoader): GoogleSpreadsheetSubmission =
    new GoogleSpreadsheetSubmission(
      classLoader.getResourceAsStream("uxforms-service-account-key.json"),
      "competitionentryform",
      convertFormData
    )

}