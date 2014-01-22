name := "api"

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io/"
)

libraryDependencies ++= {
  val sprayVersion = "1.2.0"
  Seq(
    "io.spray" % "spray-can" % sprayVersion,
    "io.spray" % "spray-routing" % sprayVersion,
    "io.spray" %% "spray-json" % "1.2.5",
    "io.spray" % "spray-testkit" % sprayVersion,
    "com.sksamuel.elastic4s" %% "elastic4s" % "0.90.10.0",
    "org.specs2" %% "specs2" % "2.2.3" % "test")
}

seq(Revolver.settings: _*)
