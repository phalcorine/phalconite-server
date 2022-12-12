package io.phalconite.api

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import io.phalconite.domain.*
import kotlinx.serialization.Serializable

@Serializable
data class ApiResource<T>(
    val data: T
)

@Serializable
data class GenericErrorResponse(
    val message: String
)

@Serializable
data class EntityUidResource(
    val uid: String
)

// HTTP

suspend fun PipelineContext<Unit, ApplicationCall>.handleError(error: DomainError) = when (error) {
    is AuthInvalidCredentials -> {
        call.respond(
            status = HttpStatusCode.Unauthorized,
            GenericErrorResponse(error.message)
        )
    }
    is AppUnknownError -> {
        call.respond(
            status = HttpStatusCode.InternalServerError,
            GenericErrorResponse(error.message)
        )
    }
    is UserNotFoundError -> {
        call.respond(
            status = HttpStatusCode.NotFound,
            GenericErrorResponse(error.message)
        )
    }
    is DuplicateUserError -> {
        call.respond(
            status = HttpStatusCode.Conflict,
            GenericErrorResponse(error.message)
        )
    }
    is DatabaseError -> {
        call.respond(
            status = HttpStatusCode.InternalServerError,
            GenericErrorResponse(error.message)
        )
    }
    is BadRequestError -> {
        call.respond(
            status = HttpStatusCode.BadRequest,
            GenericErrorResponse(error.message)
        )
    }
    is UnprocessableEntity -> {
        call.respond(
            status = HttpStatusCode.UnprocessableEntity,
            GenericErrorResponse(error.message)
        )
    }
}