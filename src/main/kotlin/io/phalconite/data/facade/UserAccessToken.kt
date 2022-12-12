package io.phalconite.data.facade

import io.phalconite.data.dbQuery
import io.phalconite.domain.dto.CreateUserAccessTokenDto
import io.phalconite.domain.dto.UserAccessTokenDto
import io.phalconite.data.entities.UserAccessTokenTable
import io.phalconite.data.entities.toUserAccessTokenDto
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import java.time.LocalDateTime

interface UserAccessTokenFacade {
    suspend fun findByUid(token: String): UserAccessTokenDto?
    suspend fun findByUserUid(userUid: String): UserAccessTokenDto?
    suspend fun create(data: CreateUserAccessTokenDto): UserAccessTokenDto?
    suspend fun deleteManyByUserUid(userUid: String)
}

object UserAccessTokenFacadeImpl : UserAccessTokenFacade {
    override suspend fun findByUid(token: String) = dbQuery {
        UserAccessTokenTable
            .select { UserAccessTokenTable.token eq token }
            .map { it.toUserAccessTokenDto() }
            .singleOrNull()
    }

    override suspend fun findByUserUid(userUid: String) = dbQuery {
        UserAccessTokenTable
            .select { UserAccessTokenTable.user_uid eq userUid }
            .map { it.toUserAccessTokenDto() }
            .singleOrNull()
    }

    override suspend fun create(data: CreateUserAccessTokenDto) = dbQuery {
        val timeNow = LocalDateTime.now()
        val statement = UserAccessTokenTable.insert {
            it[token] = data.token
            it[expires_in] = data.expiresIn
            it[user_uid] = data.userUid
            it[created_at] = timeNow
        }
        statement.resultedValues?.singleOrNull()?.toUserAccessTokenDto()
    }

    override suspend fun deleteManyByUserUid(userUid: String) = dbQuery {
        UserAccessTokenTable
            .deleteWhere { user_uid eq userUid }
        return@dbQuery
    }
}