package jp.co.ex.util

import arrow.core.Either
import arrow.core.Either.Left
import com.amazonaws.services.sqs.model.DeleteMessageResult
import com.amazonaws.services.sqs.model.Message
import com.amazonaws.services.sqs.model.SendMessageResult

interface ISqsClient {

    sealed class Error {
        data class SendMessage(val cause: Throwable) : Error()
        data class ReceiveMessage(val cause: Throwable) : Error()
        data class DeleteMessage(val cause: Throwable) : Error()
        object NotImplemented : Error()
    }

    fun <T : Any> sendMessage(
        queue: String,
        message: T
    ): Either<Error.SendMessage, SendMessageResult> =
        Left(Error.SendMessage(NotImplementedError()))

    fun receiveMessage(queue: String): Either<Error.ReceiveMessage, List<Message>> =
        Left(Error.ReceiveMessage(NotImplementedError()))

    fun deleteMessage(queue: String, receiptHandle: String): Either<Error.DeleteMessage, DeleteMessageResult> =
        Left(Error.DeleteMessage(NotImplementedError()))
}
