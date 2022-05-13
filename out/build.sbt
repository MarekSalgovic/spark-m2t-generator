version := "1.0.0"


lazy val root = (project in file("."))
  .settings(
    name := "dip",
  )

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.2.1",
  "org.apache.spark" %% "spark-sql" % "3.2.1",
)
