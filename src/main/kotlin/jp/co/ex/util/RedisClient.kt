package jp.co.ex.util

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import jp.co.ex.component.ILogger
import jp.co.ex.component.Logger
import jp.co.ex.entity.FooData
import jp.co.ex.util.IRedisClient.Error
import org.springframework.data.redis.core.*
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.data.redis.hash.ObjectHashMapper
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*
import javax.annotation.PostConstruct
import kotlin.reflect.KClass

@Component
class RedisClient(
    val redisTemplate: StringRedisTemplate,
    val retrySetPopScript: RedisScript<List<String>>,
    val objectHashMapper: ObjectHashMapper,
    val logger: ILogger
) : IRedisClient {

    lateinit var hashOps: HashOperations<String, String, String>
    lateinit var listOps: ListOperations<String, String>
    lateinit var sortedSetOps: ZSetOperations<String, String>

    @PostConstruct
    fun setUp() {
        hashOps = redisTemplate.opsForHash()
        listOps = redisTemplate.opsForList()
        sortedSetOps = redisTemplate.opsForZSet()
    }

    override fun expire(key: String, timeout: Duration): Either<Error.Expire, Boolean?> {
        return try {
            Right(redisTemplate.expire(key, timeout))
        } catch (ex: Throwable) {
            Left(Error.Expire(ex))
        }
    }

    override fun <T : Any> execute(
        script: RedisScript<T>,
        keys: List<String>,
        vararg args: Any
    ): Either<Error.Execute, T> {
        return try {
            Right(redisTemplate.execute(script, keys, args))
        } catch (ex: Throwable) {
            Left(Error.Execute(ex))
        }
    }

    override fun <T : Any> readHash(key: String, field: String): Either<Error.Read, T> {
        return super.readHash(key, field)
    }

    override fun <T : Any> readHashAll(key: String, klass: KClass<T>): Either<Error.Read, T> {
        return try {
            val hashData = hashOps.entries(key).entries.associate { e -> e.key.toByteArray() to e.value.toByteArray() }
            Right(objectHashMapper.fromHash(hashData, klass.java))
        } catch (ex: Throwable) {
            Left(Error.Read(ex))
        }
    }

    override fun <T : Any> createHash(key: String, hashObj: T): Either<Error.Create, Unit> {
        return try {
            val rawHash: Map<String, String> =
                objectHashMapper.toHash(hashObj).entries.associate { String(it.key) to String(it.value) }
            Right(hashOps.putAll(key, rawHash))
        } catch (ex: Throwable) {
            Left(Error.Create(ex))
        }
    }

    override fun updateHash(key: String, field: String, value: String): Either<Error.Create, Unit> {
        return super.updateHash(key, field, value)
    }

    override fun <T : Any> pushList(key: String, obj: T): Either<Error.Update, Long?> {
        return super.pushList(key, obj)
    }

    override fun <T : Any> popList(key: String): Either<Error.Update, T> {
        return super.popList(key)
    }

    override fun <T : Any> popAndPushList(srcKey: String, dstKey: String, timeout: Duration): Either<Error.Update, T> {
        return super.popAndPushList(srcKey, dstKey, timeout)
    }

    override fun <T : Any> readListAll(key: String, klass: KClass<T>): Either<Error.Read, List<T>> {
        return super.readListAll(key, klass)
    }

    override fun <T : Any> removeList(key: String, obj: T): Either<Error.Delete, Long?> {
        return super.removeList(key, obj)
    }

    override fun <T : Any> addSortedSet(key: String, obj: T, score: Double): Either<Error.Create, Boolean?> {
        return super.addSortedSet(key, obj, score)
    }

    override fun <T : Any> removeSortedSet(key: String, obj: T): Either<Error.Delete, Long?> {
        return super.removeSortedSet(key, obj)
    }
}
