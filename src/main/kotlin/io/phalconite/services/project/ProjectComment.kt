package io.phalconite.services.project

import arrow.core.Either
import arrow.core.continuations.either
import io.github.thibaultmeyer.cuid.CUID
import io.phalconite.api.EntityUidResource
import io.phalconite.data.facade.ProjectCommentFacade
import io.phalconite.data.facade.ProjectFacade
import io.phalconite.data.facade.UserFacade
import io.phalconite.domain.*
import io.phalconite.domain.dto.*

class ProjectCommentService(
    private val projectCommentFacade: ProjectCommentFacade,
    private val projectFacade: ProjectFacade,
    private val userFacade: UserFacade
) {

    suspend fun listCommentsByProject(projectUid: String): Either<DomainError, List<ProjectCommentDto>> = either {
        val comments = projectCommentFacade.listByProject(projectUid)

        comments
    }

    suspend fun create(data: CreateProjectCommentRequestDto, projectUid: String, authorUid: String): Either<DomainError, EntityUidResource> = either {
        val project = projectFacade.findByUid(projectUid) ?: return@either shift(IllegalArgumentError("A project with the specified ID: $projectUid was not found!"))
        val author = userFacade.findByUid(authorUid) ?: return@either shift(IllegalArgumentError("A user with the specified ID: $authorUid was not found!"))

        val payload = CreateProjectCommentDto(
            uid = CUID.randomCUID().toString(),
            content = data.content,
            projectUid = projectUid,
            authorUid = authorUid
        )

        val comment = projectCommentFacade.create(payload) ?: return@either shift(AppUnknownError("An error occurred while attempting to create a project comment..."))

        EntityUidResource(comment.uid)
    }

    suspend fun update(uid: String, data: UpdateProjectCommentRequestDto, projectUid: String, authorUid: String): Either<DomainError, EntityUidResource> = either {
        val comment = projectCommentFacade.findByUid(uid) ?: return@either shift(EntityNotFoundError)

        if (comment.authorUid != authorUid) {
            return@either shift(ForbiddenActionError)
        }

        val payload = UpdateProjectCommentDto(
            content = data.content
        )

        projectCommentFacade.update(uid, payload)

        EntityUidResource(uid)
    }

    suspend fun delete(uid: String, projectUid: String, authorUid: String): Either<DomainError, EntityUidResource> = either {
        val comment = projectCommentFacade.findByUid(uid) ?: return@either shift(EntityNotFoundError)

        if (comment.authorUid != authorUid) {
            return@either shift(ForbiddenActionError)
        }

        if (comment.projectUid != projectUid) {
            return@either shift(ForbiddenActionError)
        }

        projectCommentFacade.delete(uid)

        EntityUidResource(uid)
    }
}