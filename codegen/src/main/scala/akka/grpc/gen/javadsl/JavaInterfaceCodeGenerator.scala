/*
 * Copyright (C) 2018 Lightbend Inc. <https://www.lightbend.com>
 */

package akka.grpc.gen.javadsl

import akka.grpc.gen.Logger
import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse
import templates.JavaCommon.txt.ApiInterface

object JavaInterfaceCodeGenerator extends JavaCodeGenerator {
  override def name = "akka-grpc-javadsl-interface"

  override def perServiceContent: Set[(Logger, Service) ⇒ CodeGeneratorResponse.File] = super.perServiceContent + generateServiceFile

  val generateServiceFile: (Logger, Service) ⇒ CodeGeneratorResponse.File = (logger, service) ⇒ {
    val b = CodeGeneratorResponse.File.newBuilder()
    b.setContent(ApiInterface(service).body)
    b.setName(s"${service.packageDir}/${service.name}.java")
    logger.info(s"Generating Akka gRPC service interface for [${service.packageName}.${service.name}]")
    b.build
  }
}