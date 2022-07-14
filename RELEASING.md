# Releasing

1. Pull in upstream changes (https://github.com/kobylynskyi/graphql-java-codegen)
2. Make sure `graphqlCodegenVersion` in `build.gradle` and `version` in `plugins/sbt/graphql-java-codegen-sbt-plugin/version.sbt` are set to the correct version (most likely already updated from changes from upstream)
3. From the root of the repo, run `gradle publish`
4. From `plugins/sbt/graphql-java-codegen-sbt-plugin` run `sbt publish`
