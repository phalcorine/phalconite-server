package io.phalconite.data.facade

import io.github.thibaultmeyer.cuid.CUID
import io.phalconite.data.dbQuery
import io.phalconite.data.entities.UserGroupTable
import io.phalconite.data.entities.toUserGroupDto
import io.phalconite.domain.dto.CreateUserGroupDto
import io.phalconite.domain.dto.UpdateUserGroupDto
import io.phalconite.domain.dto.UserGroupDto
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDateTime

interface UserGroupFacade {
    suspend fun list(): List<UserGroupDto>
    suspend fun findByUid(uid: String): UserGroupDto?
    suspend fun findByName(name: String): UserGroupDto?
    suspend fun create(data: CreateUserGroupDto): UserGroupDto?
    suspend fun updateInfo(uid: String, data: UpdateUserGroupDto)
    suspend fun delete(uid: String)
}


object UserGroupFacadeImpl : UserGroupFacade {
    override suspend fun list(): List<UserGroupDto> = dbQuery {
        UserGroupTable
            .selectAll()
            .orderBy(UserGroupTable.name, SortOrder.ASC)
            .map { it.toUserGroupDto() }
    }

    override suspend fun findByUid(uid: String): UserGroupDto? = dbQuery {
        UserGroupTable
            .select { UserGroupTable.uid eq uid }
            .map { it.toUserGroupDto() }
            .singleOrNull()
    }

    override suspend fun findByName(name: String): UserGroupDto? = dbQuery {
        UserGroupTable
            .select { UserGroupTable.name eq name }
            .map { it.toUserGroupDto() }
            .singleOrNull()
    }

    override suspend fun create(data: CreateUserGroupDto): UserGroupDto? = dbQuery {
        val statement = UserGroupTable
            .insert {
                val timeNow = LocalDateTime.now()
                it[uid] = CUID.randomCUID().toString()
                it[name] = data.name
                it[description] = data.description
                it[created_at] = timeNow
                it[updated_at] = timeNow
            }
        statement.resultedValues?.singleOrNull()?.toUserGroupDto()
    }

    override suspend fun updateInfo(uid: String, data: UpdateUserGroupDto) = dbQuery {
        UserGroupTable
            .update({ UserGroupTable.uid eq uid }) {
                it[name] = data.name
                it[description] = data.description
                it[updated_at] = LocalDateTime.now()
            }
        return@dbQuery
    }

    override suspend fun delete(uid: String) = dbQuery {
        UserGroupTable
            .deleteWhere { UserGroupTable.uid eq uid }
        return@dbQuery
    }
}