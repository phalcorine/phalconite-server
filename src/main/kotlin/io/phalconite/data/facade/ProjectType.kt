package io.phalconite.data.facade

import io.phalconite.data.dbQuery
import io.phalconite.data.entities.ProjectTypeTable
import io.phalconite.data.entities.toProjectTypeDto
import io.phalconite.domain.dto.CreateProjectTypeDto
import io.phalconite.domain.dto.ProjectTypeDto
import io.phalconite.domain.dto.UpdateProjectTypeDto
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDateTime

interface ProjectTypeFacade {
    suspend fun list(): List<ProjectTypeDto>
    suspend fun findByUid(uid: String): ProjectTypeDto?
    suspend fun findByName(name: String): ProjectTypeDto?
    suspend fun create(data: CreateProjectTypeDto): ProjectTypeDto?
    suspend fun update(uid: String, data: UpdateProjectTypeDto)
    suspend fun delete(uid: String)
}

object ProjectTypeFacadeImpl : ProjectTypeFacade {
    override suspend fun list(): List<ProjectTypeDto> = dbQuery {
        ProjectTypeTable
            .selectAll()
            .orderBy(ProjectTypeTable.name, SortOrder.ASC)
            .map { it.toProjectTypeDto() }
    }

    override suspend fun findByUid(uid: String): ProjectTypeDto? = dbQuery {
        ProjectTypeTable
            .select { ProjectTypeTable.uid eq uid }
            .map { it.toProjectTypeDto() }
            .singleOrNull()
    }

    override suspend fun findByName(name: String): ProjectTypeDto? = dbQuery {
        ProjectTypeTable
            .select { ProjectTypeTable.name eq name }
            .map { it.toProjectTypeDto() }
            .singleOrNull()
    }

    override suspend fun create(data: CreateProjectTypeDto): ProjectTypeDto? = dbQuery {
        val statement = ProjectTypeTable
            .insert {
                val now = LocalDateTime.now()
                it[uid] = data.uid
                it[name] = data.name
                it[description] = data.description
                it[created_at] = now
                it[updated_at] = now
            }
        statement.resultedValues
            ?.singleOrNull()
            ?.toProjectTypeDto()
    }

    override suspend fun update(uid: String, data: UpdateProjectTypeDto) = dbQuery {
        ProjectTypeTable
            .update ({ ProjectTypeTable.uid eq uid }) {
                val now = LocalDateTime.now()
                it[name] = data.name
                it[description] = data.description
                it[updated_at] = now
            }
        return@dbQuery
    }

    override suspend fun delete(uid: String) = dbQuery {
        ProjectTypeTable
            .deleteWhere { ProjectTypeTable.uid eq uid }
        return@dbQuery
    }

}