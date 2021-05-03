package jp.co.ex.component

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import jp.co.ex.component.ILogger.Error
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.BeanCreationException
import org.springframework.beans.factory.InjectionPoint
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component
@Scope("prototype")
class Logger(val injection: InjectionPoint) : ILogger {

    val logger = LoggerFactory.getLogger(
        injection.methodParameter?.containingClass
            ?: injection.field?.declaringClass
            ?: throw BeanCreationException("Cannot find type for Log")
    )

    private fun either(apply: () -> Unit): Either<Error, Unit> {
        return try {
            Right(apply())
        } catch (ex: Throwable) {
            Left(Error.IO(ex))
        }
    }

    override fun info(message: String): Either<Error, Unit> {
        return either { logger.info(message) }
    }

    override fun warn(message: String): Either<Error, Unit> {
        return either { logger.warn(message) }
    }

    override fun error(message: String): Either<Error, Unit> {
        return either { logger.error(message) }
    }
}