package io.phalconite.services.project

import arrow.core.Either
import arrow.core.continuations.either
import io.github.thibaultmeyer.cuid.CUID
import io.phalconite.api.EntityUidResource
import io.phalconite.data.facade.ProjectFacade
import io.phalconite.data.facade.ProjectTypeFacade
import io.phalconite.domain.*
import io.phalconite.domain.dto.*
import io.phalconite.domain.enums.ProjectStatusEnum

class ProjectService(
    private val projectFacade: ProjectFacade,
    private val projectTypeFacade: ProjectTypeFacade
) {

    suspend fun list(): Either<DomainError, List<ProjectDto>> = either {
        val projects = projectFacade.list()

        projects
    }

    suspend fun listByProjectType(typeUid: String): Either<DomainError, List<ProjectDto>> = either {
        val projects = projectFacade.listByProjectType(typeUid)

        projects
    }

    suspend fun findByUid(uid: String): Either<DomainError, ProjectDto> = either {
        val project = projectFacade.findByUid(uid) ?: shift(EntityNotFoundError)

        project
    }

    suspend fun create(payload: CreateProjectRequestDto, createdByUid: String): Either<DomainError, EntityUidResource> = either {
        projectFacade.findByName(payload.name)
            ?.let {
                return@either shift(ConflictRecordError("A project with the specified name: ${payload.name} already exists!"))
            }

        projectTypeFacade.findByUid(payload.typeUid)
            ?: return@either shift(BadRequestError("A project type with the specified ID: ${payload.typeUid} was not found!"))

        val createProjectDto = CreateProjectDto(
            uid = CUID.randomCUID().toString(),
            name = payload.name,
            description = payload.description,
            status = payload.status,
            typeUid = payload.typeUid,
            createdByUid = createdByUid,
            startDate = payload.startDate,
            endDate = payload.endDate
        )

        val project = projectFacade.create(createProjectDto) ?: return@either shift(AppUnknownError("An error occurred while attempting to create a project..."))

        EntityUidResource(project.uid)
    }

    suspend fun update(uid: String, payload: UpdateProjectRequestDto): Either<DomainError, EntityUidResource> = either {
        projectFacade.findByUid(uid) ?: return@either shift(EntityNotFoundError)

        // @TODO: Validate endDate without startDate
        // @TODO: Validate startDate > endDate

        val updateProjectDto = UpdateProjectDto(
            name = payload.name,
            description = payload.description,
            status = payload.status,
            typeUid = payload.typeUid,
            startDate = payload.startDate,
            endDate = payload.endDate
        )

        projectFacade.update(uid, updateProjectDto)

        EntityUidResource(uid)
    }

    suspend fun delete(uid: String): Either<DomainError, EntityUidResource> = either {
        projectFacade.findByUid(uid) ?: return@either shift(EntityNotFoundError)

        projectFacade.delete(uid)

        EntityUidResource(uid)
    }
}