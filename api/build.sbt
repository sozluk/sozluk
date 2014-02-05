name := "api"

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io/",
  "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
)

libraryDependencies ++= {
  val sprayVersion = "1.2.0"
  Seq(
    "io.spray" % "spray-can" % sprayVersion,
    "io.spray" % "spray-routing" % sprayVersion,
    "io.spray" %% "spray-json" % "1.2.5",
    "io.spray" % "spray-testkit" % sprayVersion,
    "com.sksamuel.elastic4s" %% "elastic4s" % "1.0.0-SNAPSHOT",
    "org.specs2" %% "specs2" % "2.2.3" % "test")
}

seq(Revolver.settings: _*)
