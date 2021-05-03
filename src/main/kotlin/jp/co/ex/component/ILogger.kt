package jp.co.ex.component

import arrow.core.Either
import arrow.core.Either.Left

interface ILogger {

    sealed class Error {
        data class IO(val cause: Throwable) : Error()
        object NotImplemented : Error()
    }

    fun info(message: String): Either<Error, Unit> =
        Left(Error.NotImplemented)

    fun warn(message: String): Either<Error, Unit> =
        Left(Error.NotImplemented)

    fun error(message: String): Either<Error, Unit> =
        Left(Error.NotImplemented)
}