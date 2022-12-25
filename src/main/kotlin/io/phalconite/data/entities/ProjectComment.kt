package io.phalconite.data.entities

import io.phalconite.domain.dto.ProjectCommentDto
import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.datetime

object ProjectCommentTable : IntIdTable("project_comment") {
    val uid = varchar("uid", 50).uniqueIndex()
    val content = varchar("content", 500)
    val created_at = datetime("created_at")
    val updated_at = datetime("updated_at")
    // relations
    val project_uid = reference("project_uid", ProjectTable.uid)
    val author_uid = reference("author_uid", UserTable.uid)
}

fun ResultRow.toProjectCommentDto() = ProjectCommentDto(
    uid = this[ProjectCommentTable.uid],
    content = this[ProjectCommentTable.content],
    createdAt = this[ProjectCommentTable.created_at].toKotlinLocalDateTime(),
    updatedAt = this[ProjectCommentTable.updated_at].toKotlinLocalDateTime(),
    projectUid = this[ProjectCommentTable.project_uid],
    authorUid = this[ProjectCommentTable.author_uid]
)