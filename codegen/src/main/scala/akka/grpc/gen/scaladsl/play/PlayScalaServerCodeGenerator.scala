/*
 * Copyright (C) 2018 Lightbend Inc. <https://www.lightbend.com>
 */

package akka.grpc.gen.scaladsl.play

import akka.grpc.gen.Logger
import akka.grpc.gen.scaladsl.{ ScalaCodeGenerator, Service }
import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse
import templates.PlayScala.txt._

//object PlayScalaServerCodeGenerator extends PlayScalaServerCodeGenerator

case class PlayScalaServerCodeGenerator(powerApis: Boolean = false, usePlayActions: Boolean = false) extends ScalaCodeGenerator {
  override def name: String = "akka-grpc-play-server-scala"

  override def perServiceContent =
    if (usePlayActions) super.perServiceContent + generateHandlerUsingActions + generateRouterUsingActions
    else super.perServiceContent + generateRouter

  private val generateRouter: (Logger, Service) => CodeGeneratorResponse.File = (logger, service) => {
    val b = CodeGeneratorResponse.File.newBuilder()
    b.setContent(Router(service, powerApis).body)
    b.setName(s"${service.packageDir}/Abstract${service.name}Router.scala")
    logger.info(s"Generating Akka gRPC file ${b.getName}")
    b.build
  }

  private val generateHandlerUsingActions: (Logger, Service) => CodeGeneratorResponse.File = (logger, service) => {
    val b = CodeGeneratorResponse.File.newBuilder()
    b.setContent(HandlerUsingActions(service, powerApis).body)
    b.setName(s"${service.packageDir}/${service.name}HandlerUsingActions.scala")
    logger.info(s"Generating Akka gRPC file ${b.getName}")
    b.build
  }

  private val generateRouterUsingActions: (Logger, Service) => CodeGeneratorResponse.File = (logger, service) => {
    val b = CodeGeneratorResponse.File.newBuilder()
    b.setContent(RouterUsingActions(service, powerApis).body)
    b.setName(s"${service.packageDir}/Abstract${service.name}RouterUsingActions.scala")
    logger.info(s"Generating Akka gRPC file ${b.getName}")
    b.build
  }
}
