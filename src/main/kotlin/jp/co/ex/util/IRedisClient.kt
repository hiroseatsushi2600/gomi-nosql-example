package jp.co.ex.util

import arrow.core.Either
import arrow.core.Either.Left
import org.springframework.data.redis.core.script.RedisScript
import java.time.Duration
import kotlin.reflect.KClass

interface IRedisClient {

    sealed class Error {
        data class Read(val cause: Throwable) : Error()
        data class Create(val cause: Throwable) : Error()
        data class Update(val cause: Throwable) : Error()
        data class Delete(val cause: Throwable) : Error()
        data class Expire(val cause: Throwable) : Error()
        data class Execute(val cause: Throwable) : Error()
        object NotImplemented : Error()
    }

    fun expire(key: String, timeout: Duration): Either<Error.Expire, Boolean?> =
        Left(Error.Expire(NotImplementedError()))

    fun <T : Any> execute(script: RedisScript<T>, keys: List<String>, vararg args: Any): Either<Error.Execute, T> =
        Left(Error.Execute(NotImplementedError()))

    fun <T : Any> readHash(key: String, field: String): Either<Error.Read, T> =
        Left(Error.Read(NotImplementedError()))

    fun <T : Any> readHashAll(key: String, klass: KClass<T>): Either<Error.Read, T> =
        Left(Error.Read(NotImplementedError()))

    fun <T : Any> createHash(key: String, hashObj: T): Either<Error.Create, Unit> =
        Left(Error.Create(NotImplementedError()))

    fun updateHash(key: String, field: String, value: String): Either<Error.Create, Unit> =
        Left(Error.Create(NotImplementedError()))

    fun <T : Any> pushList(key: String, obj: T): Either<Error.Update, Long?> =
        Left(Error.Update(NotImplementedError()))

    fun <T : Any> popList(key: String): Either<Error.Update, T> =
        Left(Error.Update(NotImplementedError()))

    fun <T : Any> popAndPushList(srcKey: String, dstKey: String, timeout: Duration): Either<Error.Update, T> =
        Left(Error.Update(NotImplementedError()))

    fun <T : Any> readListAll(key: String, klass: KClass<T>): Either<Error.Read, List<T>> =
        Left(Error.Read(NotImplementedError()))

    fun <T : Any> removeList(key: String, obj: T): Either<Error.Delete, Long?> =
        Left(Error.Delete(NotImplementedError()))

    fun <T : Any> addSortedSet(key: String, obj: T, score: Double): Either<Error.Create, Boolean?> =
        Left(Error.Create(NotImplementedError()))

    fun <T : Any> removeSortedSet(key: String, obj: T): Either<Error.Delete, Long?> =
        Left(Error.Delete(NotImplementedError()))
}

inline fun <reified T : Any> IRedisClient.readHashAll(key: String) =
    readHashAll(key, T::class)

inline fun <reified T : Any> IRedisClient.readListAll(key: String) =
    readListAll(key, T::class)
