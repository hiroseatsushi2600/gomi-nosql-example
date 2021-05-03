package jp.co.ex.util

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model.*
import com.google.gson.Gson
import jp.co.ex.util.ISqsClient.Error
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class SqsClient(
    val amazonSQS: AmazonSQS,
    val gson: Gson
) : ISqsClient {

    @Value("\${app.messaging.wait-time-seconds}")
    private val waitTimeSeconds: Int = 20

    override fun <T : Any> sendMessage(
        queue: String,
        message: T
    ): Either<Error.SendMessage, SendMessageResult> {
        return try {
            val queueUrl = amazonSQS.getQueueUrl(queue).queueUrl
            val sendMsg = SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(gson.toJson(message))
            Right(amazonSQS.sendMessage(sendMsg))
        } catch (ex: Throwable) {
            Left(Error.SendMessage(ex))
        }
    }

    override fun receiveMessage(queue: String): Either<Error.ReceiveMessage, List<Message>> {
        return try {
            val queueUrl = amazonSQS.getQueueUrl(queue).queueUrl
            val receive = ReceiveMessageRequest()
                .withQueueUrl(queueUrl)
                .withWaitTimeSeconds(waitTimeSeconds)
            Right(amazonSQS.receiveMessage(receive).messages)
        } catch (ex: Throwable) {
            Left(Error.ReceiveMessage(ex))
        }
    }

    override fun deleteMessage(queue: String, receiptHandle: String): Either<Error.DeleteMessage, DeleteMessageResult> {
        return try {
            Right(amazonSQS.deleteMessage(queue, receiptHandle))
        } catch (ex: Throwable) {
            Left(Error.DeleteMessage(ex))
        }
    }

}