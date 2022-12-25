package io.phalconite.data.facade

import io.phalconite.data.dbQuery
import io.phalconite.data.entities.ProjectTable
import io.phalconite.data.entities.toProjectDto
import io.phalconite.domain.dto.CreateProjectDto
import io.phalconite.domain.dto.ProjectDto
import io.phalconite.domain.dto.UpdateProjectDto
import kotlinx.datetime.toJavaLocalDateTime
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDateTime

interface ProjectFacade {
    suspend fun list(): List<ProjectDto>
    suspend fun listByProjectType(typeUid: String): List<ProjectDto>
    suspend fun findByUid(uid: String): ProjectDto?
    suspend fun findByName(name: String): ProjectDto?
    suspend fun create(data: CreateProjectDto): ProjectDto?
    suspend fun update(uid: String, data: UpdateProjectDto)
    suspend fun delete(uid: String)
}

object ProjectFacadeImpl : ProjectFacade {
    override suspend fun list(): List<ProjectDto> = dbQuery {
        ProjectTable
            .selectAll()
            .map { it.toProjectDto() }
    }

    override suspend fun listByProjectType(typeUid: String): List<ProjectDto> = dbQuery {
        ProjectTable
            .select { ProjectTable.type_uid eq typeUid }
            .map { it.toProjectDto() }
    }

    override suspend fun findByUid(uid: String): ProjectDto? = dbQuery {
        ProjectTable
            .select { ProjectTable.uid eq uid }
            .map { it.toProjectDto() }
            .singleOrNull()
    }

    override suspend fun findByName(name: String): ProjectDto? = dbQuery {
        ProjectTable
            .select { ProjectTable.name eq name }
            .map { it.toProjectDto() }
            .singleOrNull()
    }

    override suspend fun create(data: CreateProjectDto): ProjectDto? = dbQuery {
        val statement = ProjectTable
            .insert {
                val now = LocalDateTime.now()
                it[uid] = data.uid
                it[name] = data.name
                it[description] = data.description
                it[status] = data.status
                it[type_uid] = data.typeUid
                it[created_by_uid] = data.createdByUid
                it[start_date] = data.startDate?.toJavaLocalDateTime()
                it[end_date] = data.endDate?.toJavaLocalDateTime()
                it[created_at] = now
                it[updated_at] = now
            }
        statement.resultedValues?.singleOrNull()?.toProjectDto()
    }

    override suspend fun update(uid: String, data: UpdateProjectDto) = dbQuery {
        ProjectTable
            .update({ ProjectTable.uid eq uid }) {
                it[name] = data.name
                it[description] = data.description
                it[status] = data.status
                it[type_uid] = data.typeUid
                it[start_date] = data.startDate?.toJavaLocalDateTime()
                it[end_date] = data.endDate?.toJavaLocalDateTime()
                it[updated_at] = LocalDateTime.now()
            }
        return@dbQuery
    }

    override suspend fun delete(uid: String) = dbQuery {
        ProjectTable
            .deleteWhere { ProjectTable.uid eq uid }
        return@dbQuery
    }
}