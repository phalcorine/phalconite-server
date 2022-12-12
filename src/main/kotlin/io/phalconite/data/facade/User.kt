package io.phalconite.data.facade

import io.github.thibaultmeyer.cuid.CUID
import io.phalconite.data.dbQuery
import io.phalconite.domain.dto.CreateUserDto
import io.phalconite.domain.dto.UpdateUserInfoDto
import io.phalconite.domain.dto.UserDto
import io.phalconite.data.entities.UserTable
import io.phalconite.data.entities.toUserDto
import io.phalconite.domain.dto.UpdateUserActivationStatusDto
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDateTime
import java.util.UUID

interface UserFacade {
    suspend fun list(): List<UserDto>
    suspend fun findByUid(uid: String): UserDto?
    suspend fun findByEmail(email: String): UserDto?
    suspend fun create(data: CreateUserDto): UserDto?
    suspend fun updateInfo(uid: String, data: UpdateUserInfoDto)
    suspend fun updateActionStatus(uid: String, data: UpdateUserActivationStatusDto)
    suspend fun delete(uid: String)
}

object UserFacadeImpl : UserFacade {
    override suspend fun list() = dbQuery {
        UserTable.selectAll().map { it.toUserDto() }
    }

    override suspend fun findByUid(uid: String) = dbQuery {
        UserTable
            .select { UserTable.uid eq uid }
            .map { it.toUserDto() }
            .singleOrNull()
    }

    override suspend fun findByEmail(email: String) = dbQuery {
        UserTable
            .select { UserTable.email eq email }
            .map { it.toUserDto() }
            .singleOrNull()
    }

    override suspend fun create(data: CreateUserDto) = dbQuery {
        val statement = UserTable.insert {
            val updatedDate = LocalDateTime.now()
            it[uid] = CUID.randomCUID().toString()
            it[full_name] = data.fullName
            it[email] = data.email
            it[password_hash] = data.passwordHash
            it[active] = data.active
            it[created_at] = updatedDate
            it[updated_at] = updatedDate
        }
        statement.resultedValues?.singleOrNull()?.toUserDto()
    }

    override suspend fun updateInfo(uid: String, data: UpdateUserInfoDto) = dbQuery {
        UserTable.update ({ UserTable.uid eq uid }) {
            it[full_name] = data.fullName
            it[email] = data.email
        }
        return@dbQuery
    }

    override suspend fun updateActionStatus(uid: String, data: UpdateUserActivationStatusDto) = dbQuery {
        UserTable.update ({ UserTable.uid eq uid }) {
            it[active] = data.active
        }
        return@dbQuery
    }

    override suspend fun delete(uid: String) = dbQuery {
        UserTable
            .deleteWhere { UserTable.uid eq uid }
        return@dbQuery
    }

}