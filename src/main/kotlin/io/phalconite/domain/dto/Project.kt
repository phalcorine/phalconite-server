package io.phalconite.domain.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectDto(
    val uid: String,
    val name: String,
    val description: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val createdByUid: String,
    val statusUid: String,
    val typeUid: String,
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null,
    val createdBy: UserDto? = null,
    val type: ProjectTypeDto? = null,
    val status: ProjectStatusDto? = null
)

@Serializable
data class CreateProjectDto(
    val name: String,
    val description: String,
    @SerialName("status_uid")
    val statusUid: String,
    @SerialName("type_uid")
    val typeUid: String,
    @SerialName("start_date")
    val startDate: LocalDateTime? = null,
    @SerialName("end_date")
    val endDate: LocalDateTime? = null,
)

@Serializable
data class UpdateProjectDto(
    val name: String,
    val description: String,
    @SerialName("status_uid")
    val statusUid: String,
    @SerialName("type_uid")
    val typeUid: String,
    @SerialName("start_date")
    val startDate: LocalDateTime? = null,
    @SerialName("end_date")
    val endDate: LocalDateTime? = null,
)