package io.phalconite.api

import arrow.core.Either
import arrow.core.continuations.either
import io.github.thibaultmeyer.cuid.CUID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.phalconite.domain.dto.CreateUserAccessTokenDto
import io.phalconite.data.facade.UserAccessTokenFacade
import io.phalconite.data.facade.UserFacade
import io.phalconite.domain.AppUnknownError
import io.phalconite.domain.AuthInvalidCredentials
import io.phalconite.domain.DomainError
import io.phalconite.plugins.CLIENT_AUTH_NAME
import io.phalconite.util.BcryptService
import io.phalconite.util.daysInMinutes
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

// Routing

fun Route.authRoutes() {
    val authService by closestDI().instance<AuthService>()
    route("/auth") {

        // Login
        post("/login") {
            val payload = call.receive<AuthLoginRequestDto>()
            either {
                val response = authService.login(payload).bind()
                call.respond(
                    status = HttpStatusCode.OK,
                    response
                )
            }.mapLeft {
                handleError(it)
            }
        }

        // Check Auth
        authenticate(CLIENT_AUTH_NAME) {
            get("check-auth") {
                val user = call.principal<AuthLoggedInUserDto>()
                println("User: ")
                println(user)
                call.respond(
                    status = HttpStatusCode.OK,
                    hashMapOf("data" to user)
                )
            }
        }

    }
}

// Services

class AuthService(
    private val userFacade: UserFacade,
    private val userAccessTokenFacade: UserAccessTokenFacade
) {

    suspend fun login(payload: AuthLoginRequestDto): Either<DomainError, AuthLoginResponseDto> = either {
        val user = userFacade.findByEmail(payload.email) ?: shift(AuthInvalidCredentials)
        if (!BcryptService.verify(payload.password, user.passwordHash)) {
            return@either shift(AuthInvalidCredentials)
        }

        // Delete old tokens
        userAccessTokenFacade.deleteManyByUserUid(user.uid)

        // Create new token=
        val userAccessToken = userAccessTokenFacade.create(
            CreateUserAccessTokenDto(
                token = CUID.randomCUID().toString(),
                expiresIn = daysInMinutes,
                userUid = user.uid
            )
        ) ?: shift(AppUnknownError("An error occurred while attempting to generate an access token!"))

        val response = AuthLoginResponseDto(
            accessToken = userAccessToken.token,
            user = AuthLoggedInUserDto(
                uid = user.uid,
                fullName = user.fullName
            )
        )

        response
    }
}

// DTOs

@Serializable
data class AuthLoginRequestDto(
    val email: String,
    val password: String
)

@Serializable
data class AuthLoginResponseDto(
    val accessToken: String,
    val user: AuthLoggedInUserDto,
)

@Serializable
data class AuthLoggedInUserDto(
    val uid: String,
    @SerialName("full_name")
    val fullName: String
) : Principal