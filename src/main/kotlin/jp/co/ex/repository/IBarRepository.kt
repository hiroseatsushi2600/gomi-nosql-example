package jp.co.ex.repository

import jp.co.ex.entity.Bar
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * mongoのサンプル
 */
interface IBarRepository : MongoRepository<Bar, String> {
    fun findByBarName(name: String): List<Bar>
}