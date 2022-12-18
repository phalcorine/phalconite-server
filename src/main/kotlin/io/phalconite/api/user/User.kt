package io.phalconite.api.user

import arrow.core.continuations.either
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.phalconite.api.ApiDataResource
import io.phalconite.api.handleError
import io.phalconite.domain.dto.CreateUserRequestDto
import io.phalconite.domain.dto.UpdateUserInfoRequestDto
import io.phalconite.domain.BadRequestError
import io.phalconite.domain.UnprocessableEntity
import io.phalconite.domain.dto.UpdateUserActivationStatusRequestDto
import io.phalconite.services.user.UserService
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

// Routing

fun Route.userRoutes() {
    val userService by closestDI().instance<UserService>()

    route("/user") {

        // List Users
        get("/list") {
            either {
                val users = userService.listUsers().bind()

                call.respond(
                    status = HttpStatusCode.OK,
                    ApiDataResource(users)
                )
            }.mapLeft {
                handleError(it)
            }
        }

        // Find User By UID
        get("/find/uid/{uid}") {
            either {
                val uid = call.parameters["uid"] ?: return@either shift(BadRequestError("Invalid Route Parameter!"))

                val user = userService.findUserByUid(uid).bind()

                call.respond(
                    status = HttpStatusCode.OK,
                    ApiDataResource(user)
                )
            }.mapLeft {
                handleError(it)
            }
        }

        // Find User By Email
        get("/find/email/{email}") {
            either {
                val email = call.parameters["email"] ?: return@either shift(BadRequestError("Invalid Route Parameter!"))

                val user = userService.findUserByEmail(email).bind()

                call.respond(
                    status = HttpStatusCode.OK,
                    ApiDataResource(user)
                )
            }.mapLeft {
                handleError(it)
            }
        }

        // Create A User
        post("/create") {
            either {
                val payload = call.receive<CreateUserRequestDto>()

                val response = userService.createUser(payload).bind()

                call.respond(
                    status = HttpStatusCode.Created,
                    ApiDataResource(response)
                )
            }.mapLeft {
                handleError(it)
            }
        }

        // Update A User Information
        patch("/update/{uid}/info") {
            either {
                val uid = call.parameters["uid"] ?: return@either shift(BadRequestError("Invalid Route Parameter!"))
                val requestBody = runCatching {
                    call.receive<UpdateUserInfoRequestDto>()
                }.getOrElse { return@either shift(UnprocessableEntity) }

                val response = userService.updateUserInfo(uid, requestBody).bind()

                call.respond(
                    status = HttpStatusCode.OK,
                    ApiDataResource(response)
                )
            }.mapLeft {
                handleError(it)
            }
        }

        patch("/update/{uid}/activation-status") {
            either {
                val uid = call.parameters["uid"] ?: return@either shift(BadRequestError("Invalid Route Parameter!"))
                val requestBody = runCatching {
                    call.receive<UpdateUserActivationStatusRequestDto>()
                }.getOrElse { return@either shift(UnprocessableEntity) }

                val response = userService.updateUserActivationStatus(uid, requestBody).bind()

                call.respond(
                    status = HttpStatusCode.OK,
                    ApiDataResource(response)
                )
            }.mapLeft {
                handleError(it)
            }
        }

        // Delete A User
        delete("/delete/{uid}") {
            either {
                val uid = call.parameters["uid"] ?: return@either shift(BadRequestError("Invalid Route Parameter!"))

                val response = userService.deleteUser(uid).bind()

                call.respond(
                    status = HttpStatusCode.OK,
                    ApiDataResource(response)
                )
            }.mapLeft {
                handleError(it)
            }
        }
    }
}