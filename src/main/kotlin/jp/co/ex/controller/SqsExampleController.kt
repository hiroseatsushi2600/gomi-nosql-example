package jp.co.ex.controller

import arrow.core.Either.Left
import arrow.core.Either.Right
import com.google.gson.Gson
import jp.co.ex.entity.FooData
import jp.co.ex.entity.FooRequest
import jp.co.ex.component.Logger
import jp.co.ex.util.SqsClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("api/sqs")
@EnableScheduling
class SqsExampleController(
    val sqsClient: SqsClient,
    val gson: Gson,
    val logger: Logger
) {

    @Value("\${app.messaging.order-queue-name}")
    private val orderQueueName: String = "order"

    @PostMapping("foo")
    fun sendMessage(@RequestBody body: FooRequest): String {
        val processingId = UUID.randomUUID().toString()
        val data = FooData(
            id = processingId,
            status = "starting...",
            message1 = body.message1,
            message2 = body.message2,
            message3 = body.message3
        )
        return when (val result = sqsClient.sendMessage(orderQueueName, data)) {
            is Left -> {
                logger.error("${result.value.cause.localizedMessage}")
                "Bye, world."
            }
            is Right -> {
                logger.info("success.")
                "ok"
            }
        }
    }

    @Scheduled(cron = "*/5 * * * * *")
    fun receiveMessage() {
        // TODO: 同一メッセージの重複取得がバチクソに発生するので、冪等風味必須。またはSQSやめて普通のMQにする。
        when (val messages = sqsClient.receiveMessage(orderQueueName)) {
            is Left -> {
                logger.error("${messages.value.cause.localizedMessage}")
                "Bye, world."
            }
            is Right -> {
                if (messages.value.isNullOrEmpty()) logger.info("no message.")
                messages.value.forEach {
                    logger.info("message: ${it.body}")
                    val data = gson.fromJson(it.body, FooData::class.java)
                    logger.info("id=${data.id}, ${data.message1}, ${data.message2}, ${data.message3}")

                    sqsClient.deleteMessage(orderQueueName, it.receiptHandle)
                }
            }
        }
    }
}