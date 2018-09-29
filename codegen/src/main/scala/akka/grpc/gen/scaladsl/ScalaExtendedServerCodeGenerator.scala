/*
 * Copyright (C) 2018 Lightbend Inc. <https://www.lightbend.com>
 */

package akka.grpc.gen.scaladsl

import akka.grpc.gen.Logger
import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse
import templates.ScalaServer.txt._

trait ScalaExtendedServerCodeGenerator extends ScalaCodeGenerator {
  override def name = "akka-grpc-scaladsl-server"

  override def perServiceContent = super.perServiceContent + ScalaCodeGenerator.generateServiceFile + generateExtendedService + generateExtendedHandler

  def generateExtendedService(logger: Logger, service: Service): CodeGeneratorResponse.File = {
    val b = CodeGeneratorResponse.File.newBuilder()
    b.setContent(ExtendedApiTrait(service).body)
    b.setName(s"${service.packageDir}/${service.name}Extended.scala")
    logger.info(s"Generating Akka gRPC file ${b.getName}")
    //    logger.info(s"Generating Akka gRPC extended service interface ${service.packageName}.${service.name}Extended")
    b.build
  }

  def generateExtendedHandler(logger: Logger, service: Service): CodeGeneratorResponse.File = {
    val b = CodeGeneratorResponse.File.newBuilder()
    b.setContent(ExtendedHandler(service).body)
    b.setName(s"${service.packageDir}/${service.name}ExtendedHandler.scala")
    logger.info(s"Generating Akka gRPC file ${b.getName}")
    //    logger.info(s"Generating Akka gRPC extended server handler ${service.packageName}.${service.name}ExtendedHandler")
    b.build
  }
}

object ScalaExtendedServerCodeGenerator extends ScalaExtendedServerCodeGenerator
