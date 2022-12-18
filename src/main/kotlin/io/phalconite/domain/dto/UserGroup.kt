package io.phalconite.domain.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserGroupDto(
    val uid: String,
    val name: String,
    val description: String,
    @SerialName("created_at")
    val createdAt: LocalDateTime,
    @SerialName("updated_at")
    val updatedAt: LocalDateTime,
)

data class CreateUserGroupDto(
    val name: String,
    val description: String
)

@Serializable
data class CreateUserGroupRequestDto(
    val name: String,
    val description: String
)

data class UpdateUserGroupDto(
    val name: String,
    val description: String
)

@Serializable
data class UpdateUserGroupRequestDto(
    val name: String,
    val description: String
)