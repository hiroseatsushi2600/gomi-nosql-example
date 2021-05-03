package jp.co.ex.controller

import jp.co.ex.component.Logger
import jp.co.ex.entity.Bar
import jp.co.ex.repository.IBarRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.util.*

@RestController
@RequestMapping("api/mongo")
class MongoExampleController(
    val barRepository: IBarRepository,
    val logger: Logger
) {

    @GetMapping("hello")
    fun sendMessage(): String {
        barRepository.save(
            Bar(
                id = UUID.randomUUID().toString(),
                barName = "aaaaaa",
                barNumber = 1,
                createdAt = LocalDateTime.now(),
                message1 = "あ",
                message2 = "い",
                message3 = "う"
            )
        )
        barRepository.save(
            Bar(
                id = UUID.randomUUID().toString(),
                barName = "aaaaaa",
                barNumber = 2,
                createdAt = LocalDateTime.now(),
                message1 = "か",
                message2 = "き",
                message3 = "く"
            )
        )

        val bars = barRepository.findByBarName("aaaaaa")
        bars.forEach {
            logger.info("name=${it.barName}, number=${it.barNumber}, 1=${it.message1}, 2=${it.message2}, 3=${it.message3}")
        }

        return "Bye, World. ${bars.size}"
    }

}