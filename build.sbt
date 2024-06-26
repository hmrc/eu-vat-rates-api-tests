lazy val root = (project in file("."))
  .disablePlugins(JUnitXmlReportPlugin)
  .settings(
    name := "eu-vat-rates-api-tests",
    version := "0.1.0",
    scalaVersion := "2.13.12",
    libraryDependencies ++= Dependencies.test,
    (Compile / compile) := ((Compile / compile) dependsOn (Compile / scalafmtSbtCheck, Compile / scalafmtCheckAll)).value
  )
