package com.equalexperts.scaladayscompetitionform

import java.util.Locale

import com.equalexperts.scaladayscompetitionform.build.MyFormDefinitionBuildInfo
import com.uxforms.domain.{FormDefinition, FormDefinitionFactory, ResourceBundleMessages}
import com.uxforms.dsl.helpers.All._
import scala.concurrent.duration._

object MyFormDefinitionFactory extends FormDefinitionFactory with TemplateLoader {

  /**
    * This is which locales are supported by your form, in order of preference.
    * So if your user doesn't explicitly state which locale they want the form in, they
    * will get the first in this sequence.
    */
  override val supportedLocales: Seq[Locale] = Seq(new Locale("en", "GB"))

  /**
    * Makes my classLoader available implicitly so that message bundles can be referenced
    * easily.
    */
  implicit val localClassLoader = getClass.getClassLoader


  /**
    * Factory method for instantiating your form definition.
    */
  override def formDefinition(requestedLocale: Locale): FormDefinition = {

    /**
      * Resolves the locale requested by the user from a combination of their HTTP headers,
      * explicitly requested locale (i.e. in the URL), and those supported by this form definition.
      */
    implicit val locale = resolveRequestedLocale(requestedLocale)

    implicit val formLevelMessages: ResourceBundleMessages = "formMessages"

    /**
      * This is where the questions for your form are defined.
      */
    formDef(

      MyFormDefinitionBuildInfo.name,

      1 day,

      completedPage("completedMessages"),

      Set.empty,

      section(
        "firstSectionMessages",

        inputText("name", "name.label" -> "Name", required),
        email("email", "email.label" -> "Email", required ++ validEmail()),
        inputText("twitter", "twitter.label" -> "Twitter", maxLength(15)),
        inputText("github", "github.label" -> "Github", noConstraints)
      )
    )

  }

}
