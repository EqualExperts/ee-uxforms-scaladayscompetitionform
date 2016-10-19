
val basePackage = "com.equalexperts.scaladayscompetitionform"

lazy val root = (project in file("."))
  .enablePlugins(SbtOsgi, FormDefinitionPlugin, BuildInfoPlugin)
  .settings(
    buildInfoPackage := s"$basePackage.build",
    buildInfoKeys := Seq[BuildInfoKey](
      name,
      "artifact" -> (artifactPath in (Compile, packageBin)).value
    ),
    buildInfoObject := "MyFormDefinitionBuildInfo"
  )

scalaVersion := "2.11.7"

organization := "com.equalexperts"

name := "equalexperts-competitionform"

test <<= (test in Test) dependsOn (OsgiKeys.bundle)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-v")

libraryDependencies ++= {
  Seq(
    "com.uxforms" %% "uxforms-dsl" % "15.5.0",
    "com.uxforms" %% "uxforms-google-spreadsheets" % "15.0.0",
    "com.uxforms" %% "test" % "15.5.0" % Test,
    "org.scalatest" %% "scalatest" % "2.2.6" % Test,
    "com.novocode" % "junit-interface" % "0.11" % Test
  )
}

// We must export our form definition factory and activator's package to the OSGi context
// so that UX Forms can load and execute this form definition.
OsgiKeys.exportPackage := Seq(s"$basePackage.*")

OsgiKeys.bundleActivator := Some(s"$basePackage.Activator")
