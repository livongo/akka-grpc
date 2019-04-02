package akka.grpc

import sbt._, Keys._

/**
 * For projects that are not to be published.
 */
object NoPublish extends AutoPlugin {
  override def requires = plugins.JvmPlugin

  override def projectSettings = Seq(
    skip in publish := true,
  )
}

//object Publish extends AutoPlugin {
//  import bintray.BintrayPlugin
//  import bintray.BintrayPlugin.autoImport._
//
//  override def trigger = allRequirements
//  override def requires = BintrayPlugin
//
//  override def projectSettings = Seq(
//    bintrayOrganization := Some("akka"),
//    bintrayPackage := "akka-grpc",
//    homepage := Some(url("https://developer.lightbend.com/docs/akka-grpc/current/")),
//    scmInfo := Some(ScmInfo(url("https://github.com/akka/akka-grpc"), "git@github.com:akka/akka-grpc")),
//    licenses := Seq("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0")),
//    developers += Developer("contributors",
//      "Contributors",
//      "https://gitter.im/akka/dev",
//      url("https://github.com/akka/akka-grpc/graphs/contributors")),
//  )
//}

object Publish extends AutoPlugin {
  override def trigger = allRequirements
  //  override def requires = BintrayPlugin
  override def projectSettings = Seq(
    publishTo := Artifactory.publishTarget,
    credentials += Artifactory.credentials
  )
}

object Artifactory {
  lazy val logger = ConsoleLogger(System.err)

  def getFromEnv(key: String, placeholder: String): String = sys.env.get(key) match {
    case Some(value) => value
    case None =>
      logger.warn(s"Missing environment variable: $key. Publish to artifactory will fail.")
      placeholder
  }

  val Realm: String = "Artifactory Realm"

  lazy val host: String = sys.env.getOrElse("ARTIFACTORY_HOST", "artifactory.prod.livongo.com")
  lazy val path: String = sys.env.getOrElse("ARTIFACTORY_PATH", "artifactory/ext-release-local")
  lazy val user: String = getFromEnv("ARTIFACTORY_USER", "MISSING_USER")
  lazy val pass: String = getFromEnv("ARTIFACTORY_PASS", "MISSING_PASSWORD")

  lazy val publishTarget: Option[Resolver] = Some(Realm at s"http://$host/$path")
  lazy val credentials:   Credentials      = Credentials(Realm, host, user, pass)
}
