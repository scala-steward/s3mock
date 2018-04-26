package io.findify.s3mock.route

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import com.typesafe.scalalogging.LazyLogging
import io.findify.s3mock.error.NoSuchKeyException
import io.findify.s3mock.provider.Provider

import scala.util.{Failure, Success, Try}

/**
  * Created by shutty on 8/20/16.
  */
case class DeleteObject(implicit provider: Provider) extends LazyLogging {
  def route(bucket:String, path:String) = delete {
    complete {
      Try(provider.deleteObject(bucket, path)) match {
        case Success(_) =>
          logger.info(s"deleted object $bucket/$path")
          HttpResponse(StatusCodes.NoContent)
        case Failure(ex) =>
          logger.error(s"cannot delete object $bucket/$path", ex)
          HttpResponse(StatusCodes.NotFound)
      }

    }
  }
}
