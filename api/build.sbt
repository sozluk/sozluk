import AssemblyKeys._

name := "api"

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io/",
  "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.9",      
  "io.spray" %% "spray-can" % Common.sprayVersion,
  "io.spray" %% "spray-routing" % Common.sprayVersion,
  "io.spray" %% "spray-json" % "1.3.1",
  "io.spray" %% "spray-testkit" % Common.sprayVersion % "test",
  "com.sksamuel.elastic4s" %% "elastic4s" % Common.elastic4sVersion,
  "org.specs2" %% "specs2-core" % "2.4.15" % "test")

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
