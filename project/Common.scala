import sbt._
import Keys._

object Common {
  lazy val prompt = { state: State =>
    val extracted = Project.extract(state)
    import extracted._

    (name in currentRef get structure.data).map { name =>
      "[" + Colors.blue(name) + "] $ "
    }.getOrElse("> ")
  }

  val elastic4sVersion = "1.4.11"
  val sprayVersion = "1.3.1"
}
