package com.equalexperts.scaladayscompetitionform

import javax.inject.Inject

import com.equalexperts.scaladayscompetitionform.build.MyFormDefinitionBuildInfo
import com.uxforms.domain.FormDefinitionFactory
import com.uxforms.test.OsgiContainer
import org.hamcrest.CoreMatchers._
import org.hamcrest.MatcherAssert._
import org.junit.Test
import org.junit.runner.RunWith
import org.ops4j.pax.exam.CoreOptions._
import org.ops4j.pax.exam.Option
import org.ops4j.pax.exam.junit.PaxExam
import org.ops4j.pax.exam.spi.reactors.{ExamReactorStrategy, PerSuite}
import org.ops4j.pax.exam.util.PathUtils

@RunWith(classOf[PaxExam])
@ExamReactorStrategy(Array(classOf[PerSuite]))
class ContainerTest extends OsgiContainer {

  override val projectArtifact: Option = composite(
    bundle(MyFormDefinitionBuildInfo.artifact.toURI.toURL.toExternalForm),
    systemProperty("logback.configurationFile").value(s"file:${PathUtils.getBaseDir}/src/test/resources/logback.xml")
  )

  @Inject
  var formDefFactory: FormDefinitionFactory = _

  @Test
  def containerShouldStartUpSuccessfully(): Unit = {
    assertThat(context, is(notNullValue()))
  }

  @Test
  def formDefinitionShouldBeAvailableInOsgiContext(): Unit = {
    assertThat(formDefFactory, is(notNullValue()))
  }

}