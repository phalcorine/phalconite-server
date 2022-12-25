package io.phalconite.services.user

import arrow.core.Either
import arrow.core.continuations.either
import io.phalconite.api.EntityUidResource
import io.phalconite.data.facade.UserFacade
import io.phalconite.data.facade.UserGroupFacade
import io.phalconite.data.facade.UsersToUserGroupsFacade
import io.phalconite.domain.AppUnknownError
import io.phalconite.domain.ConflictRecordError
import io.phalconite.domain.DomainError
import io.phalconite.domain.EntityNotFoundError
import io.phalconite.domain.dto.*

class UserGroupService(
    private val userGroupFacade: UserGroupFacade,
    private val userFacade: UserFacade,
    private val usersToUserGroupsFacade: UsersToUserGroupsFacade
) {

    suspend fun list(): Either<DomainError, List<UserGroupDto>> = either {
        val groups = userGroupFacade.list()

        groups
    }

    suspend fun findByUid(uid: String): Either<DomainError, UserGroupDto> = either {
        val group = userGroupFacade.findByUid(uid) ?: shift(EntityNotFoundError)

        group
    }

    suspend fun create(request: CreateUserGroupRequestDto): Either<DomainError, EntityUidResource> = either {
        userGroupFacade.findByName(request.name)?.let {
            return@either shift(ConflictRecordError("A user group with the name: ${request.name} already exists!"))
        }

        val createdGroup = userGroupFacade.create(
            CreateUserGroupDto(
                name = request.name,
                description = request.description
            )
        ) ?: shift(AppUnknownError("An error occurred while attempting to create a user group record"))

        EntityUidResource(createdGroup.uid)
    }

    suspend fun update(uid: String, request: UpdateUserGroupRequestDto): Either<DomainError, EntityUidResource> = either {
        userGroupFacade.findByUid(uid) ?: shift(EntityNotFoundError)

        val payload = UpdateUserGroupDto(
            name = request.name,
            description = request.description
        )

        userGroupFacade.updateInfo(uid, payload)

        EntityUidResource(uid)
    }

    suspend fun delete(uid: String): Either<DomainError, EntityUidResource> = either {
        userGroupFacade.findByUid(uid) ?: shift(EntityNotFoundError)

        userGroupFacade.delete(uid)

        EntityUidResource(uid)
    }

    suspend fun addUserToGroup(groupUid: String, userUid: String): Either<DomainError, Unit> = either {
        userGroupFacade.findByUid(groupUid) ?: shift(EntityNotFoundError)
        userFacade.findByUid(userUid) ?: shift(EntityNotFoundError)

        usersToUserGroupsFacade.create(
            CreateUsersToUserGroupsDto(
                groupUid = groupUid,
                userUid = userUid
            )
        )
    }
}