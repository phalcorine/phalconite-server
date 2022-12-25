package io.phalconite.domain.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectCommentDto(
    val uid: String,
    val content: String,
    @SerialName("created_at")
    val createdAt: LocalDateTime,
    @SerialName("updated_at")
    val updatedAt: LocalDateTime,
    // relations
    @SerialName("project_uid")
    val projectUid: String,
    @SerialName("author_uid")
    val authorUid: String,
)

data class CreateProjectCommentDto(
    val uid: String,
    val content: String,
    val projectUid: String,
    val authorUid: String,
)

data class UpdateProjectCommentDto(
    val content: String,
)

// API

@Serializable
data class CreateProjectCommentRequestDto(
    val content: String,
)

@Serializable
data class UpdateProjectCommentRequestDto(
    val content: String,
)