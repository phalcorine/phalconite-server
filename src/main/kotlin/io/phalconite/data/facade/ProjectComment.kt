package io.phalconite.data.facade

import io.phalconite.data.dbQuery
import io.phalconite.data.entities.ProjectCommentTable
import io.phalconite.data.entities.toProjectCommentDto
import io.phalconite.domain.dto.CreateProjectCommentDto
import io.phalconite.domain.dto.ProjectCommentDto
import io.phalconite.domain.dto.UpdateProjectCommentDto
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime

interface ProjectCommentFacade {
    suspend fun listByProject(projectUid: String): List<ProjectCommentDto>
    suspend fun findByUid(uid: String): ProjectCommentDto?
    suspend fun create(data: CreateProjectCommentDto): ProjectCommentDto?
    suspend fun update(uid: String, data: UpdateProjectCommentDto)
    suspend fun delete(uid: String)
}

object ProjectCommentFacadeImpl : ProjectCommentFacade {
    override suspend fun listByProject(projectUid: String): List<ProjectCommentDto> = dbQuery {
        ProjectCommentTable
            .select { ProjectCommentTable.project_uid eq projectUid }
            .map { it.toProjectCommentDto() }
    }

    override suspend fun findByUid(uid: String): ProjectCommentDto? = dbQuery {
        ProjectCommentTable
            .select { ProjectCommentTable.uid eq uid }
            .singleOrNull()
            ?.toProjectCommentDto()
    }

    override suspend fun create(data: CreateProjectCommentDto): ProjectCommentDto? = dbQuery {
        val statement = ProjectCommentTable
            .insert {
                val now = LocalDateTime.now()
                it[uid] = data.uid
                it[content] = data.content
                it[created_at] = now
                it[updated_at] = now
                it[project_uid] = data.projectUid
                it[author_uid] = data.authorUid
            }
        statement.resultedValues?.singleOrNull()?.toProjectCommentDto()
    }

    override suspend fun update(uid: String, data: UpdateProjectCommentDto) = dbQuery {
        ProjectCommentTable
            .update({ ProjectCommentTable.uid eq uid }) {
                it[content] = data.content
                it[updated_at] = LocalDateTime.now()
            }
        return@dbQuery
    }

    override suspend fun delete(uid: String) = dbQuery {
        ProjectCommentTable
            .deleteWhere { ProjectCommentTable.uid eq uid }
        return@dbQuery
    }

}