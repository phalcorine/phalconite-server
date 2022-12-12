package io.phalconite.api.user

import arrow.core.Either
import arrow.core.continuations.either
import io.github.thibaultmeyer.cuid.CUID
import io.ktor.server.routing.*
import io.phalconite.api.EntityUidResource
import io.phalconite.data.facade.UserFacade
import io.phalconite.domain.AppUnknownError
import io.phalconite.domain.DomainError
import io.phalconite.domain.DuplicateUserError
import io.phalconite.domain.UserNotFoundError
import io.phalconite.domain.dto.*
import io.phalconite.util.BcryptService

fun Route.moduleUserRoutes() {
    route("/user") {
        userRoutes()
    }
}

// Services

class UserService(
    private val userFacade: UserFacade
) {

    suspend fun listUsers(): Either<DomainError, List<UserWithoutPasswordDto>> = either {
        val users = userFacade.list()
        users.map { it.toUserWithoutPasswordDto() }
    }

    suspend fun findUserByUid(uid: String): Either<DomainError, UserWithoutPasswordDto> = either {
        val user = userFacade.findByUid(uid) ?: shift(UserNotFoundError)
        user.toUserWithoutPasswordDto()
    }

    suspend fun findUserByEmail(email: String): Either<DomainError, UserWithoutPasswordDto> = either {
        val user = userFacade.findByEmail(email) ?: shift(UserNotFoundError)
        user.toUserWithoutPasswordDto()
    }

    suspend fun createUser(request: CreateUserRequestDto): Either<DomainError, EntityUidResource> = either {
        val existingUser = userFacade.findByEmail(request.email)
        if (existingUser != null) {
            return@either shift(DuplicateUserError)
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
        userFacade.findByUid(uid) ?: return@either shift(UserNotFoundError)

        val payload = UpdateUserInfoDto(
            fullName = request.fullName,
            email = request.email,
        )

        userFacade.updateInfo(uid, payload)

        EntityUidResource(uid)
    }

    suspend fun updateUserActivationStatus(uid: String, request: UpdateUserActivationStatusRequestDto): Either<DomainError, EntityUidResource> = either {
        userFacade.findByUid(uid) ?: return@either shift(UserNotFoundError)

        val payload = UpdateUserActivationStatusDto(
            active = request.active
        )

        userFacade.updateActionStatus(uid, payload)

        EntityUidResource(uid)
    }

    suspend fun deleteUser(uid: String): Either<DomainError, EntityUidResource> = either {
        userFacade.findByUid(uid) ?: return@either shift(UserNotFoundError)

        userFacade.delete(uid)

        EntityUidResource(uid)
    }
}