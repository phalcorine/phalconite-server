package io.phalconite.domain.dto

import io.phalconite.domain.enums.ProjectStatusEnum
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectDto(
    val uid: String,
    val name: String,
    val description: String,
    val status: ProjectStatusEnum,
    @SerialName("created_at")
    val createdAt: LocalDateTime,
    @SerialName("updated_at")
    val updatedAt: LocalDateTime,
    @SerialName("created_by_uid")
    val createdByUid: String,
    @SerialName("type_uid")
    val typeUid: String,
    @SerialName("start_date")
    val startDate: LocalDateTime? = null,
    @SerialName("end_date")
    val endDate: LocalDateTime? = null,
    val createdBy: UserDto? = null,
    val type: ProjectTypeDto? = null,
)

@Serializable
data class CreateProjectRequestDto(
    val name: String,
    val description: String,
    val status: ProjectStatusEnum,
    @SerialName("type_uid")
    val typeUid: String,
    @SerialName("start_date")
    val startDate: LocalDateTime? = null,
    @SerialName("end_date")
    val endDate: LocalDateTime? = null,
)

@Serializable
data class UpdateProjectRequestDto(
    val name: String,
    val description: String,
    val status: ProjectStatusEnum,
    @SerialName("type_uid")
    val typeUid: String,
    @SerialName("start_date")
    val startDate: LocalDateTime? = null,
    @SerialName("end_date")
    val endDate: LocalDateTime? = null,
)

// Domain

data class CreateProjectDto(
    val uid: String,
    val name: String,
    val description: String,
    val status: ProjectStatusEnum,
    val typeUid: String,
    val createdByUid: String,
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null,
)

data class UpdateProjectDto(
    val name: String,
    val description: String,
    val status: ProjectStatusEnum,
    val typeUid: String,
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null,
)