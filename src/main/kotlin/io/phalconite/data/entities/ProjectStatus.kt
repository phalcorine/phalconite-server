package io.phalconite.data.entities

import io.phalconite.domain.dto.ProjectStatusDto
import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.datetime

object ProjectStatusTable : IntIdTable("project_status") {
    val uid = varchar("uid", 50).uniqueIndex()
    val name = varchar("name", 30)
    val description = varchar("description", 100)
    val created_at = datetime("created_at")
    val updated_at = datetime("updated_at")
}

fun ResultRow.toProjectStatusDto(): ProjectStatusDto {
    return ProjectStatusDto(
        uid = this[ProjectStatusTable.uid],
        name = this[ProjectStatusTable.name],
        description = this[ProjectStatusTable.description],
        createdAt = this[ProjectStatusTable.created_at].toKotlinLocalDateTime(),
        updatedAt = this[ProjectStatusTable.updated_at].toKotlinLocalDateTime()
    )
}