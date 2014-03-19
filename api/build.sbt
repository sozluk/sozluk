import AssemblyKeys._

name := "api"

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io/",
  "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
)

libraryDependencies ++= {
  val sprayVersion = "1.3.1"
  Seq(
    "io.spray" % "spray-can" % sprayVersion,
    "io.spray" % "spray-routing" % sprayVersion,
    "io.spray" %% "spray-json" % "1.2.5",
    "io.spray" % "spray-testkit" % sprayVersion,
    "com.sksamuel.elastic4s" %% "elastic4s" % "1.0.0.0",
    "org.specs2" %% "specs2" % "2.2.3" % "test")
}

seq(Revolver.settings: _*)

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
  {
    case "reference.conf" => MergeStrategy.concat
    case "META-INF/MANIFEST.MF" => MergeStrategy.discard
    case "META-INF/components.txt" => MergeStrategy.discard
    case "META-INF/jdom-info.xml" => MergeStrategy.discard
    case x => old(x)
  }
}
