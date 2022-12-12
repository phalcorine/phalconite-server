package io.phalconite.data.entities

import io.phalconite.domain.dto.ProjectDto
import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.datetime

object ProjectTable : IntIdTable("project") {
    val uid = varchar("uid", 50).uniqueIndex()
    val name = varchar("name", 100)
    val description = varchar("description", 500)
    val start_date = datetime("start_date").nullable()
    val end_date = datetime("end_date").nullable()
    val created_at = datetime("created_at")
    val updated_at = datetime("updated_at")
    // relations
    val created_by_uid = reference("created_by_uid", UserTable.uid)
    val type_uid = reference("type_uid", ProjectTypeTable.uid)
    val status_uid = reference("status_uid", ProjectStatusTable.uid)
}

fun ResultRow.toProjectDto(): ProjectDto {
    return ProjectDto(
        uid = this[ProjectTable.uid],
        name = this[ProjectTable.name],
        description = this[ProjectTable.description],
        createdByUid = this[ProjectTable.created_by_uid],
        statusUid = this[ProjectTable.status_uid],
        typeUid = this[ProjectTable.type_uid],
        createdAt = this[ProjectTable.created_at].toKotlinLocalDateTime(),
        updatedAt = this[ProjectTable.updated_at].toKotlinLocalDateTime(),
        startDate = this[ProjectTable.start_date]?.toKotlinLocalDateTime(),
        endDate = this[ProjectTable.end_date]?.toKotlinLocalDateTime(),
    )
}