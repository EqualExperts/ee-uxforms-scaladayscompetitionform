package com.equalexperts.scaladayscompetitionform

import java.util.Locale

import com.uxforms.domain.{FormDefinition, RequestInfo}
import org.joda.time.{DateTimeUtils, Instant}
import org.scalatest.{FreeSpec, Matchers, OptionValues}
import play.api.libs.json.Json

class submitToGoogleSpec extends FreeSpec with Matchers with OptionValues {

  "convertFormData" - {

    val baseHeaders = Seq(
      "User-Agent" -> "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_4) AppleWebKit/601.5.17 (KHTML, like Gecko) Version/9.1 Safari/601.5.17",
      "Cache-Control" -> "max-age=0",
      "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"
    )

    "should include the client's IP from the X-Forwarded-For HTTP Header" in {
      val headers = baseHeaders :+ "X-Forwarded-For" -> "5.148.6.178"
      val result = submitToGoogle.convertFormData(formData, formDef, RequestInfo(Map.empty, headers))

      result.columnNames should contain("xForwardedFor")
      result.data.head should contain("5.148.6.178")
    }

    "should leave the xForwardedFor column blank if the X-Forwarded-For HTTP header is not present" in {
      val result = submitToGoogle.convertFormData(formData, formDef, RequestInfo(Map.empty, baseHeaders))

      result.columnNames should contain("xForwardedFor")
      result.data.head.last shouldBe ""
    }

    "should include the timestamp of the submission in ISO8601 format" in {
      withFrozenTime() { instant =>
        val result = submitToGoogle.convertFormData(formData, formDef, RequestInfo(Map.empty, Seq.empty))
        result.columnNames should contain("timestamp")
        val timestampColumnIndex = formDef.flattenWidgets.length
        result.data.head.lift(timestampColumnIndex).value shouldBe instant.toString
      }
    }

  }


  def withFrozenTime[T](frozenTime: Instant = Instant.now)(body: Instant => T) = {
    DateTimeUtils.setCurrentMillisFixed(frozenTime.getMillis)
    body(frozenTime)
    DateTimeUtils.setCurrentMillisSystem()
  }

  private def formDef: FormDefinition = MyFormDefinitionFactory.formDefinition(new Locale("en", "GB"))

  private val formData = Json.obj("name" -> "Bob Smith", "email" -> "bob@example.com", "phone" -> "123561823", "twitter" -> "bsmith", "github" -> "bsmith")

}
