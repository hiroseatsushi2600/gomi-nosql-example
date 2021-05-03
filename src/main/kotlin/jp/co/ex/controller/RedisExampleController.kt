package jp.co.ex.controller

import arrow.core.Either.Left
import arrow.core.Either.Right
import jp.co.ex.component.ILogger
import jp.co.ex.entity.FooData
import jp.co.ex.util.IRedisClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
@RequestMapping("api/redis")
class RedisExampleController(
    val redisClient: IRedisClient,
    val logger: ILogger
) {
    @GetMapping("hello")
    fun hello(): FooData? {
        val processingId = UUID.randomUUID().toString()
        val data = FooData(
            id = processingId,
            status = "starting...",
            message1 = "aaaaaaaaaa",
            message2 = "bbbbbbbbb",
            message3 = "cccc"
        )
        redisClient.createHash("key", data)

        return when (val result = redisClient.readHashAll("key", FooData::class)) {
            is Left -> {
                logger.error("bad.")
                null
            }
            is Right -> {
                val v = result.value
                logger.info("id = ${v.id}, status = ${v.status}, message = ${v.message1} ${v.message2} ${v.message3}")
                v
            }
        }
    }

}