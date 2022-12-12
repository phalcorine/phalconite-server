package io.phalconite.domain.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val uid: String,
    @SerialName("full_name")
    val fullName: String,
    val email: String,
    val passwordHash: String,
    val active: Boolean,
    @SerialName("created_at")
    val createdAt: LocalDateTime,
    @SerialName("updated_at")
    val updatedAt: LocalDateTime,
)

@Serializable
data class UserWithoutPasswordDto(
    val uid: String,
    @SerialName("full_name")
    val fullName: String,
    val email: String,
    val active: Boolean,
    @SerialName("created_at")
    val createdAt: LocalDateTime,
    @SerialName("updated_at")
    val updatedAt: LocalDateTime,
)

data class CreateUserDto(
    val uid: String,
    val fullName: String,
    val email: String,
    val passwordHash: String,
    val active: Boolean
)

data class UpdateUserInfoDto(
    val fullName: String,
    val email: String,
)

data class UpdateUserActivationStatusDto(
    val active: Boolean
)

@Serializable
data class CreateUserRequestDto(
    @SerialName("full_name")
    val fullName: String,
    val email: String,
    val password: String,
    val active: Boolean
)

@Serializable
data class UpdateUserInfoRequestDto(
    @SerialName("full_name")
    val fullName: String,
    val email: String,
)

@Serializable
data class UpdateUserActivationStatusRequestDto(
    val active: Boolean
)

// Extensions

fun UserDto.toUserWithoutPasswordDto(): UserWithoutPasswordDto {
    return UserWithoutPasswordDto(
        uid,
        fullName,
        email,
        active,
        createdAt,
        updatedAt
    )
}