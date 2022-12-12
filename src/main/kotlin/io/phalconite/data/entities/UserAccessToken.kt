package io.phalconite.data.entities

import io.phalconite.domain.dto.UserAccessTokenDto
import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.datetime

object UserAccessTokenTable : IntIdTable("user_access_token") {
    val token = varchar("token", 100)
    val expires_in = long("expires_in")
    val created_at = datetime("created_at")
    // relations
    val user_uid = reference("user_uid", UserTable.uid)
}

fun ResultRow.toUserAccessTokenDto(): UserAccessTokenDto {
    return UserAccessTokenDto(
        token = this[UserAccessTokenTable.token],
        expiresIn = this[UserAccessTokenTable.expires_in],
        createdAt = this[UserAccessTokenTable.created_at].toKotlinLocalDateTime(),
        userUid = this[UserAccessTokenTable.user_uid],
    )
}