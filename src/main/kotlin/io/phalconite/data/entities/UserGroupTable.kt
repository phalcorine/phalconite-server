package io.phalconite.data.entities

import io.phalconite.domain.dto.UserGroupDto
import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.datetime

object UserGroupTable : IntIdTable("user_group") {
    val uid = varchar("uid", 50).uniqueIndex()
    val name = varchar("name", 50)
    val description = varchar("description", 100)
    val created_at = datetime("created_at")
    val updated_at = datetime("updated_at")
}

fun ResultRow.toUserGroupDto(): UserGroupDto {
    return UserGroupDto(
        uid = this[UserGroupTable.uid],
        name = this[UserGroupTable.name],
        description = this[UserGroupTable.description],
        createdAt = this[UserGroupTable.created_at].toKotlinLocalDateTime(),
        updatedAt = this[UserGroupTable.updated_at].toKotlinLocalDateTime()
    )
}