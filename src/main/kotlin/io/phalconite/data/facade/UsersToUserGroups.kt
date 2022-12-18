package io.phalconite.data.facade

import io.phalconite.data.dbQuery
import io.phalconite.data.entities.UsersToUserGroupsTable
import io.phalconite.domain.dto.CreateUsersToUserGroupsDto
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert

interface UsersToUserGroupsFacade {
    suspend fun create(data: CreateUsersToUserGroupsDto)
    suspend fun delete(groupUid: String, userUid: String)
}

object UsersToUserGroupsFacadeImpl : UsersToUserGroupsFacade {
    override suspend fun create(data: CreateUsersToUserGroupsDto) = dbQuery {
        UsersToUserGroupsTable
            .insert {
                it[user_group_uid] = data.groupUid
                it[user_uid] = data.userUid
            }
        return@dbQuery
    }

    override suspend fun delete(groupUid: String, userUid: String) = dbQuery {
        UsersToUserGroupsTable
            .deleteWhere {
                user_group_uid eq groupUid
                user_uid eq userUid
            }
        return@dbQuery
    }
}