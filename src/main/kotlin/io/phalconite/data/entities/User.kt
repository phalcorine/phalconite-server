package io.phalconite.data.entities

import io.phalconite.domain.dto.UserDto
import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.datetime

object UserTable : IntIdTable("user") {
    val uid = varchar("uid", 50).uniqueIndex()
    val full_name = varchar("full_name", 100)
    val email = varchar("email", 180)
    val password_hash = varchar("password_hash", 4096)
    val active = bool("active")
    val created_at = datetime("created_at")
    val updated_at = datetime("updated_at")
}

fun ResultRow.toUserDto(): UserDto {
    return UserDto(
        uid = this[UserTable.uid],
        fullName = this[UserTable.full_name],
        email = this[UserTable.email],
        passwordHash = this[UserTable.password_hash],
        active = this[UserTable.active],
        createdAt = this[UserTable.created_at].toKotlinLocalDateTime(),
        updatedAt = this[UserTable.updated_at].toKotlinLocalDateTime()
    )
}