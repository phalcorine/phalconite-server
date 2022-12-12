package io.phalconite.data.entities

import io.phalconite.domain.dto.ProjectTypeDto
import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.datetime

object ProjectTypeTable : IntIdTable("project_type") {
    val uid = varchar("uid", 50).uniqueIndex()
    val name = varchar("name", 30)
    val description = varchar("description", 100)
    val created_at = datetime("created_at")
    val updated_at = datetime("updated_at")
}

fun ResultRow.toProjectTypeDto(): ProjectTypeDto {
    return ProjectTypeDto(
        uid = this[ProjectTypeTable.uid],
        name = this[ProjectTypeTable.name],
        description = this[ProjectTypeTable.description],
        createdAt = this[ProjectTypeTable.created_at].toKotlinLocalDateTime(),
        updatedAt = this[ProjectTypeTable.updated_at].toKotlinLocalDateTime()
    )
}