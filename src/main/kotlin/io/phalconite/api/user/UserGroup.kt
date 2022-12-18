package io.phalconite.api.user

import arrow.core.continuations.either
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.phalconite.api.ApiDataResource
import io.phalconite.api.handleError
import io.phalconite.domain.BadRequestError
import io.phalconite.domain.dto.CreateUserGroupRequestDto
import io.phalconite.domain.dto.UpdateUserGroupRequestDto
import io.phalconite.services.user.UserGroupService
import io.phalconite.services.user.UserService
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Route.userGroupRoutes() {
    val userGroupService by closestDI().instance<UserGroupService>()
    val userService by closestDI().instance<UserService>()

    route("/group") {

        // List Groups
        get("/list") {
            either {
                val groups = userGroupService.list().bind()

                call.respond(
                    status = HttpStatusCode.OK,
                    ApiDataResource(groups)
                )
            }.mapLeft {
                handleError(it)
            }
        }

        // Find Group by UID
        get("/find/uid/{uid}") {
            either {
                val uid = call.parameters["uid"] ?: return@either shift(BadRequestError("Invalid Route Parameter!"))

                val group = userGroupService.findByUid(uid).bind()

                call.respond(
                    status = HttpStatusCode.OK,
                    ApiDataResource(group)
                )
            }.mapLeft {
                handleError(it)
            }
        }

        // Create Group
        post("/create") {
            either {
                val requestBody = call.receive<CreateUserGroupRequestDto>()

                val response = userGroupService.create(requestBody).bind()

                call.respond(
                    status = HttpStatusCode.OK,
                    ApiDataResource(response)
                )
            }.mapLeft {
                handleError(it)
            }
        }

        // Update Group
        patch("/update/{uid}") {
            either {
                val uid = call.parameters["uid"] ?: return@either shift(BadRequestError("Invalid Route Parameter!"))
                val requestBody = call.receive<UpdateUserGroupRequestDto>()

                val response = userGroupService.update(uid, requestBody).bind()

                call.respond(
                    status = HttpStatusCode.OK,
                    ApiDataResource(response)
                )
            }.mapLeft {
                handleError(it)
            }
        }

        // Delete Group
        delete("/delete/{uid}") {
            either {
                val uid = call.parameters["uid"] ?: return@either shift(BadRequestError("Invalid Route Parameter!"))

                val response = userGroupService.delete(uid).bind()

                call.respond(
                    status = HttpStatusCode.OK,
                    ApiDataResource(response)
                )
            }.mapLeft {
                handleError(it)
            }
        }

        // Relations: Users
        route("/relation/{uid}/users") {
            // List
            get("/list") {
                either {
                    val uid = call.parameters["uid"] ?: return@either shift(BadRequestError("Invalid Route Parameter!"))

                    val groupUsers = userService.findManyUsersByGroupUid(uid).bind()

                    call.respond(
                        status = HttpStatusCode.OK,
                        ApiDataResource(groupUsers)
                    )
                }.mapLeft {
                    handleError(it)
                }
            }

            // Add User to Group
            post("/add/{user_uid}") {
                either {
                    val groupUid = call.parameters["uid"] ?: return@either shift(BadRequestError("Invalid Route Parameter!"))
                    val userUid = call.parameters["user_uid"] ?: return@either shift(BadRequestError("Invalid Route Parameter!"))

                    val response = userGroupService.addUserToGroup(groupUid, userUid).bind()

                    call.respond(HttpStatusCode.NoContent)
                }.mapLeft {
                    handleError(it)
                }
            }
        }

    }
}