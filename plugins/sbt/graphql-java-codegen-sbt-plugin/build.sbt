import sbtrelease.ReleaseStateTransformations._

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
    organization := "com.lifeway.contentplatform",
    scalaVersion := "2.12.12",
    name := "graphql-codegen-sbt-plugin",
    releaseIgnoreUntrackedFiles := true,
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      setReleaseVersion,
      publishArtifacts
    ),
    updateOptions          := updateOptions.value.withGigahorse(false),
    resolvers ++= Seq[Resolver](
      "LifeWay Account Artifactory" at "https://artifactory.prod.lifeway.com/artifactory/lifewayaccount/",
      "LifeWay Content Platform Artifactory" at "https://artifactory.prod.lifeway.com/artifactory/contentplatform/",
      "Lifeway Repo Internal Libs" at "https://artifactory.prod.lifeway.com/artifactory/libs-release-local/",
      Resolver.jcenterRepo
    ),    
    credentials += Credentials(
      "Artifactory Realm",
      "artifactory.prod.lifeway.com",
      sys.env.getOrElse("ARTIFACTORY_LW_USER", "bad user"),
      sys.env.getOrElse("ARTIFACTORY_LW_KEY", "bad key")
    ),
    pomExtra := {
      <scm>
        <url>git@github.com:LifewayIT/graphql-java-codegen.git</url>
        <connection>scm:git:git@github.com:LifewayIT/graphql-java-codegen.git</connection>
      </scm>
        <developers>
          <developer>
            <id>lifeway</id>
            <name>LifeWay Christian Resources</name>
            <url>https://www.lifeway.com</url>
          </developer>
        </developers>
    },
    homepage          := Some(url(s"https://github.com/LifewayIT/graphql-java-codegen")),
    publishMavenStyle := true,
    pomIncludeRepository := { _ =>
      false
    },    
    resolvers += 
"Artifactory" at "https://artifactory.prod.lifeway.com/artifactory/contentplatform/",
    publishTo := {
      if (version.value.trim.endsWith("SNAPSHOT"))
        Some(
          "Artifactory Realm" at
            "https://artifactory.prod.lifeway.com/artifactory/contentplatform;build.timestamp=" +
            new java.util.Date().getTime
        )
      else
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