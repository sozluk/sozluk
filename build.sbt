lazy val commonSettings = Seq(
  organization := "org.sozluk",
  version := "1.0-SNAPSHOT",
  scalaVersion  := "2.10.3",
  scalacOptions := Seq(
    "-unchecked",
    "-deprecation",
    "-encoding", "utf8",
    "-feature",
    "-language:postfixOps",
    "-language:implicitConversions",
    "-language:existentials"))

lazy val settings = (
  commonSettings
  ++ scalariformSettings
  ++ org.scalastyle.sbt.ScalastylePlugin.Settings)

lazy val restApi = project.in(file("rest-api"))
  .dependsOn(parser)
  .aggregate(parser)
  .settings(settings: _*)
  .settings(testOptions in Test += Tests.Argument("showtimes", "true"))

lazy val parser = project.in(file("parser"))
  .settings(settings: _*)
  .settings(testOptions in Test += Tests.Argument("-oDS"))

shellPrompt in ThisBuild := Common.prompt
