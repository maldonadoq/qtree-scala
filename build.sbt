import Dependencies._

name := "QuadTree"
version := "11-R16"
scalaVersion := "2.12.5"

scalaSource in Compile := baseDirectory(_ / "src").value
resourceDirectory in Compile := baseDirectory(_ / "src").value


scalacOptions ++= Seq("-deprecation", "-feature")
resolvers += Opts.resolver.sonatypeSnapshots
libraryDependencies += "org.scalafx" %% "scalafx" % "11-R16"

val javafxModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux") => "linux"
  case n if n.startsWith("Mac") => "mac"
  case n if n.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform!")
}

libraryDependencies ++= javafxModules.map(m => "org.openjfx" % s"javafx-$m" % "11" classifier osName)
shellPrompt := { state => System.getProperty("user.name") + ":" + Project.extract(state).currentRef.project + "> " }
fork := true