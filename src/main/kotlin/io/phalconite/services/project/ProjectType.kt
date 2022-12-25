package io.phalconite.services.project

import arrow.core.Either
import arrow.core.continuations.either
import io.github.thibaultmeyer.cuid.CUID
import io.phalconite.api.EntityUidResource
import io.phalconite.data.facade.ProjectTypeFacade
import io.phalconite.domain.AppUnknownError
import io.phalconite.domain.ConflictRecordError
import io.phalconite.domain.DomainError
import io.phalconite.domain.EntityNotFoundError
import io.phalconite.domain.dto.*

class ProjectTypeService(
    private val projectTypeFacade: ProjectTypeFacade
) {

    suspend fun list(): Either<DomainError, List<ProjectTypeDto>> = either {
        val projectTypes = projectTypeFacade.list()

        projectTypes
    }

    suspend fun findByUid(uid: String): Either<DomainError, ProjectTypeDto> = either {
        val projectType = projectTypeFacade.findByUid(uid) ?: shift(EntityNotFoundError)

        projectType
    }

    suspend fun create(data: CreateProjectTypeRequestDto): Either<DomainError, EntityUidResource> = either {
        projectTypeFacade.findByName(data.name)
            ?.let {
                return@either shift(ConflictRecordError("A project type with the name: ${data.name} already exists!"))
            }

        val projectType = projectTypeFacade.create(
            CreateProjectTypeDto(
                uid = CUID.randomCUID().toString(),
                name = data.name,
                description = data.description
            )
        ) ?: shift(AppUnknownError("An error occurred while attempting to create a new project type!"))

        EntityUidResource(projectType.uid)
    }

    suspend fun update(uid: String, data: UpdateProjectTypeRequestDto): Either<DomainError, EntityUidResource> = either {
        projectTypeFacade.findByUid(uid) ?: shift(EntityNotFoundError)

        val payload = UpdateProjectTypeDto(
            name = data.name,
            description = data.description
        )

        projectTypeFacade.update(uid, payload)

        EntityUidResource(uid)
    }

    suspend fun delete(uid: String): Either<DomainError, EntityUidResource> = either {
        projectTypeFacade.findByUid(uid) ?: shift(EntityNotFoundError)

        projectTypeFacade.delete(uid)

        EntityUidResource(uid)
    }
}