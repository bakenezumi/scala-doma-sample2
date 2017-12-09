import Dependencies._

lazy val root = (project in file(".")).settings(
  inThisBuild(List(
    scalaVersion := "2.12.4",
    version      := "0.1.0-SNAPSHOT"
  )),
  name := "scala-doma-sample2",
  libraryDependencies += scalaTest % Test,
  // for Doma annotation processor
  (unmanagedSourceDirectories in Compile) += generateSourceDirectory
) dependsOn repository aggregate repository

lazy val repository = project.settings(
  inThisBuild(List(
    scalaVersion := "2.12.4",
    version      := "0.1.0-SNAPSHOT"
  )),
  name := "scala-doma-sample2-repository",
  libraryDependencies ++= Seq(
    "org.seasar.doma" % "doma" % "2.19.0",
    "com.h2database" % "h2" % "1.4.193",
    scalaTest % Test
  ),
  // for Doma annotation processor
  processAnnotationsSettings
)

lazy val processAnnotations = taskKey[Unit]("Process annotations")
lazy val generateSourceDirectory = file(".").getAbsoluteFile / "generated"

lazy val processAnnotationsSettings: Seq[Def.Setting[_]] = Seq(
  processAnnotations in Compile := {
    val classes = (unmanagedSources in Compile).value.filter(_.getPath.endsWith("scala"))
    val log = streams.value.log
    val separator = System.getProperties.getProperty("path.separator")
    val classpath = ((dependencyClasspath in Compile).value.files mkString separator) + separator + (classDirectory in Compile).value.toString
    if (classes.nonEmpty) {
      log.info("Processing annotations ...")
      deleteFiles(generateSourceDirectory)
      val cutSize = (scalaSource in Compile).value.getPath.length + 1
      val classesToProcess = classes.map(_.getPath.substring(cutSize).replaceFirst("\\.scala", "").replaceAll("[\\\\/]", ".")).mkString(" ")
      val directory = (classDirectory in Compile).value
      val command = s"javac -cp $classpath -proc:only -XprintRounds -d $directory -s $generateSourceDirectory $classesToProcess"
      executeCommand(command, "Failed to process annotations.", log)
      log.info("Done processing annotations.")
    }
  },
  processAnnotations in Compile := ((processAnnotations in Compile) dependsOn (compile in Compile)).value,
  copyResources in Compile := Def.taskDyn {
    val copy = (copyResources in Compile).value
    Def.task {
      (processAnnotations in Compile).value
      copy
    }
  }.value
)

def deleteFiles(targetDirectory: File): Unit = {
  def delete(f: File): Unit =
    if (f.isFile)
      f.delete()
    else
      f.listFiles.toList.foreach(delete)
  if(targetDirectory.exists)
    delete(targetDirectory)
  else
    targetDirectory.mkdir()
}

def executeCommand(command: String, errorMessage: => String, log: Logger): Unit = {
  val process = java.lang.Runtime.getRuntime.exec(command)
  printInputStream(process.getErrorStream, log)
  if (process.waitFor() != 0) {
    log.error(errorMessage)
    sys.error("Failed running command: " + command)
  }
}

def printInputStream(is: scala.tools.nsc.interpreter.InputStream, log: Logger): Unit = {
  val br = new java.io.BufferedReader(new java.io.InputStreamReader(is))
  try {
    var line = br.readLine
    while (line != null) {
      log.info(line)
      line = br.readLine
    }
  } finally {
    br.close()
  }
}
