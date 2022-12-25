package io.phalconite.domain.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectTypeDto(
    val uid: String,
    val name: String,
    val description: String,
    @SerialName("created_at")
    val createdAt: LocalDateTime,
    @SerialName("updated_at")
    val updatedAt: LocalDateTime
)

data class CreateProjectTypeDto(
    val uid: String,
    val name: String,
    val description: String
)

data class UpdateProjectTypeDto(
    val name: String,
    val description: String
)

// API

@Serializable
data class CreateProjectTypeRequestDto(
    val name: String,
    val description: String
)

@Serializable
data class UpdateProjectTypeRequestDto(
    val name: String,
    val description: String
)