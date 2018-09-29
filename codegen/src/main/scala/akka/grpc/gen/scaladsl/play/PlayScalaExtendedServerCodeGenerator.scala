/*
 * Copyright (C) 2018 Lightbend Inc. <https://www.lightbend.com>
 */

package akka.grpc.gen.scaladsl.play

import akka.grpc.gen.Logger
import akka.grpc.gen.scaladsl.{ ScalaCodeGenerator, Service }
import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse
import templates.PlayScala.txt._

object PlayScalaExtendedServerCodeGenerator extends PlayScalaExtendedServerCodeGenerator

trait PlayScalaExtendedServerCodeGenerator extends ScalaCodeGenerator {
  override def name: String = "akka-grpc-play-server-scala"

  override def perServiceContent = super.perServiceContent + generateExtendedRouter

  private val generateExtendedRouter: (Logger, Service) => CodeGeneratorResponse.File = (logger, service) => {
    val b = CodeGeneratorResponse.File.newBuilder()
    b.setContent(ExtendedRouter(service).body)
    b.setName(s"${service.packageDir}/Abstract${service.name}ExtendedRouter.scala")
    b.build
  }
}
