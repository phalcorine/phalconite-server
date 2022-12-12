package io.phalconite.domain.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class UserAccessTokenDto(
    val token: String,
    val expiresIn: Long,
    val createdAt: LocalDateTime,
    val userUid: String,
    val user: UserDto? = null
)

data class CreateUserAccessTokenDto(
    val token: String,
    val expiresIn: Long,
    val userUid: String
)