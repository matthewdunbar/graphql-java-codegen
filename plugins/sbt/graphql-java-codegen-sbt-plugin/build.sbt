import sbtrelease.ReleaseStateTransformations._

name := "graphql-codegen-sbt-plugin"

// must be equals to oss Group Id

val jValidationVersion = settingKey[String]("default java Validation api").withRank(KeyRanks.Invisible)
jValidationVersion := "2.0.1.Final"

// keep version is equals with parent project `graphql-java-codegen`.
// Plugin don't need to care about the scala version, just the SBT version.
lazy val `graphql-codegen-sbt-plugin` = Project(id = "graphql-codegen-sbt-plugin", base = file(".")).
  enablePlugins(SbtPlugin, BuildInfoPlugin).
  settings(
    sbtPlugin := true,
    scriptedBufferLog := false,
    scriptedLaunchOpts += s"-Dplugin.version=${version.value}",
    scalacOptions += "-target:jvm-1.8",
    organization := s"com.lifeway.contentplatform",
    name := "graphql-codegen-sbt-plugin",
    releaseIgnoreUntrackedFiles := true,
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      releaseStepCommandAndRemaining("^ scripted"),
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      releaseStepCommandAndRemaining("^ publish"),
      setNextVersion,
      commitNextVersion,
    ),
    updateOptions          := updateOptions.value.withGigahorse(false),
    resolvers ++= Seq[Resolver](
      "Artifactory" at "https://artifactory.prod.lifeway.com/artifactory/contentplatform/",
      "Artifactory-OSS-Snapshot" at "https://oss.jfrog.org/artifactory/libs-snapshot/",
      "Artifactory-OSS-Release" at "https://oss.jfrog.org/artifactory/libs-release/"
    ),    
    credentials += Credentials(
      "Artifactory Realm",
      "artifactory.prod.lifeway.com",
      sys.env.getOrElse("ARTIFACTORY_LW_USER", "bad user"),
      sys.env.getOrElse("ARTIFACTORY_LW_KEY", "bad key")
    ),
    homepage          := Some(url(s"https://github.com/matthewdunbar/graphql-java-codegen")),
    publishMavenStyle := true,
    publishTo := {
        Some("Artifactory Realm" at "https://artifactory.prod.lifeway.com/artifactory/contentplatform")
    },    
    libraryDependencies ++= Seq(
      "com.lifeway.contentplatform" % "graphql-java-codegen" % (version in ThisBuild).value,
      "org.freemarker" % "freemarker" % "2.3.31",
      "com.graphql-java" % "graphql-java" % "16.2",
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.12.1",
      "com.typesafe" % "config" % "1.4.1"
    ),
    buildInfoKeys := Seq[BuildInfoKey](name, version, sbtVersion, jValidationVersion),
    buildInfoPackage := "io.github.dreamylost.graphql.codegen"
  )