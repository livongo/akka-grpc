/*
 * Copyright (C) 2018-2019 Lightbend Inc. <https://www.lightbend.com>
 */

package akka.grpc.scaladsl

import scala.concurrent.{ ExecutionException, Future }
import io.grpc.Status
import akka.grpc.GrpcServiceException
import akka.grpc.internal.GrpcResponseHelpers
import akka.http.scaladsl.model.HttpResponse

object GrpcExceptionHandler {
  val defaultMapper: PartialFunction[Throwable, Status] = {
    case grpcException: GrpcServiceException ⇒ grpcException.status
    case _: NotImplementedError ⇒ Status.UNIMPLEMENTED
    case _: UnsupportedOperationException ⇒ Status.UNIMPLEMENTED
    case other ⇒ Status.INTERNAL
  }

  val default: PartialFunction[Throwable, Future[HttpResponse]] = default(defaultMapper)

  def default(mapper: PartialFunction[Throwable, Status]): PartialFunction[Throwable, Future[HttpResponse]] = {
    case e: ExecutionException ⇒
      if (e.getCause == null) Future.failed(e)
      else handling(mapper)(e.getCause)
    case other ⇒
      handling(mapper)(other)
  }

  private def handling(mapper: PartialFunction[Throwable, Status]): PartialFunction[Throwable, Future[HttpResponse]] = {
    mapper
      .orElse[Throwable, Status] {
        case grpcException: GrpcServiceException ⇒ grpcException.status
        case _: NotImplementedError ⇒ Status.UNIMPLEMENTED
        case _: UnsupportedOperationException ⇒ Status.UNIMPLEMENTED
        case other ⇒ Status.INTERNAL
      }
      .andThen(s => Future.successful(GrpcResponseHelpers.status(s)))
  }
}
