package jp.co.ex.entity

import java.time.LocalDateTime

data class Bar (
    val id: String,
    val barName: String,
    val barNumber: Int,
    val createdAt: LocalDateTime,
    val message1: String,
    val message2: String,
    val message3: String
)