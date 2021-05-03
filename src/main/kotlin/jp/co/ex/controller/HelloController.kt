package jp.co.ex.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api")
class HelloController {

    @GetMapping("hello")
    fun sendMessage(): String {
        return "Bye, World."
    }

}