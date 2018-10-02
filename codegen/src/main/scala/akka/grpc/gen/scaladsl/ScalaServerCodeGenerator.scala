/*
 * Copyright (C) 2018 Lightbend Inc. <https://www.lightbend.com>
 */

package akka.grpc.gen.scaladsl

import akka.grpc.gen.Logger
import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse
import templates.ScalaServer.txt._

case class ScalaServerCodeGenerator(powerApis: Boolean = false) extends ScalaCodeGenerator {
  override def name = "akka-grpc-scaladsl-server"

  override def perServiceContent =
    if (powerApis) super.perServiceContent + ScalaCodeGenerator.generateServiceFile + generatePowerService + generatePowerHandler
    else super.perServiceContent + ScalaCodeGenerator.generateServiceFile + generateHandler

  def generateHandler(logger: Logger, service: Service): CodeGeneratorResponse.File = {
    val b = CodeGeneratorResponse.File.newBuilder()
    b.setContent(Handler(service).body)
    b.setName(s"${service.packageDir}/${service.name}Handler.scala")
    b.build
  }

  def generatePowerService(logger: Logger, service: Service): CodeGeneratorResponse.File = {
    val b = CodeGeneratorResponse.File.newBuilder()
    b.setContent(PowerApiTrait(service).body)
    b.setName(s"${service.packageDir}/${service.name}PowerApi.scala")
    logger.info(s"Generating Akka gRPC file ${b.getName}")
    //    logger.info(s"Generating Akka gRPC extended service interface ${service.packageName}.${service.name}Extended")
    b.build
  }

  def generatePowerHandler(logger: Logger, service: Service): CodeGeneratorResponse.File = {
    val b = CodeGeneratorResponse.File.newBuilder()
    b.setContent(PowerApiHandler(service).body)
    b.setName(s"${service.packageDir}/${service.name}PowerApiHandler.scala")
    logger.info(s"Generating Akka gRPC file ${b.getName}")
    //    logger.info(s"Generating Akka gRPC extended server handler ${service.packageName}.${service.name}ExtendedHandler")
    b.build
  }

}

//object ScalaServerCodeGenerator extends ScalaServerCodeGenerator
