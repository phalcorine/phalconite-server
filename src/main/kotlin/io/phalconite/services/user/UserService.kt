package io.phalconite.services.user

import arrow.core.Either
import arrow.core.continuations.either
import io.github.thibaultmeyer.cuid.CUID
import io.phalconite.api.EntityUidResource
import io.phalconite.data.facade.UserFacade
import io.phalconite.data.facade.UserGroupFacade
import io.phalconite.domain.AppUnknownError
import io.phalconite.domain.DomainError
import io.phalconite.domain.ConflictRecordError
import io.phalconite.domain.EntityNotFoundError
import io.phalconite.domain.dto.*
import io.phalconite.util.BcryptService

class UserService(
    private val userFacade: UserFacade,
    private val userGroupFacade: UserGroupFacade
) {

    suspend fun listUsers(): Either<DomainError, List<UserWithoutPasswordDto>> = either {
        val users = userFacade.list()
        users.map { it.toUserWithoutPasswordDto() }
    }

    suspend fun findUserByUid(uid: String): Either<DomainError, UserWithoutPasswordDto> = either {
        val user = userFacade.findByUid(uid) ?: shift(EntityNotFoundError)
        user.toUserWithoutPasswordDto()
    }

    suspend fun findUserByEmail(email: String): Either<DomainError, UserWithoutPasswordDto> = either {
        val user = userFacade.findByEmail(email) ?: shift(EntityNotFoundError)
        user.toUserWithoutPasswordDto()
    }

    suspend fun createUser(request: CreateUserRequestDto): Either<DomainError, EntityUidResource> = either {
        userFacade.findByEmail(request.email)?.let {
            return@either shift(ConflictRecordError("A user with the specified email: ${request.email} already exists!"))
        }

        val payload = CreateUserDto(
            uid = CUID.randomCUID().toString(),
            fullName = request.fullName,
            email = request.email,
            active = request.active,
            passwordHash = BcryptService.hash(request.password)
        )

        val createdUser = userFacade.create(payload) ?: shift(AppUnknownError("An error occurred while attempting to create a user!"))

        EntityUidResource(createdUser.uid)
    }

    suspend fun updateUserInfo(uid: String, request: UpdateUserInfoRequestDto): Either<DomainError, EntityUidResource> = either {
        userFacade.findByUid(uid) ?: return@either shift(EntityNotFoundError)

        val payload = UpdateUserInfoDto(
            fullName = request.fullName,
            email = request.email,
        )

        userFacade.updateInfo(uid, payload)

        EntityUidResource(uid)
    }

    suspend fun updateUserActivationStatus(uid: String, request: UpdateUserActivationStatusRequestDto): Either<DomainError, EntityUidResource> = either {
        userFacade.findByUid(uid) ?: return@either shift(EntityNotFoundError)

        val payload = UpdateUserActivationStatusDto(
            active = request.active
        )

        userFacade.updateActionStatus(uid, payload)

        EntityUidResource(uid)
    }

    suspend fun deleteUser(uid: String): Either<DomainError, EntityUidResource> = either {
        userFacade.findByUid(uid) ?: return@either shift(EntityNotFoundError)

        userFacade.delete(uid)

        EntityUidResource(uid)
    }

    suspend fun findManyUsersByGroupUid(groupUid: String): Either<DomainError, List<UserWithoutPasswordDto>> = either {
        userGroupFacade.findByUid(groupUid) ?: shift(EntityNotFoundError)

        val users = userFacade.findManyByGroupUid(groupUid)

        users.map { it.toUserWithoutPasswordDto() }
    }
}