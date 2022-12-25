package io.phalconite.api.project

import arrow.core.continuations.either
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.phalconite.api.ApiDataResource
import io.phalconite.api.handleError
import io.phalconite.domain.BadRequestError
import io.phalconite.domain.UnprocessableEntityError
import io.phalconite.domain.dto.CreateProjectTypeRequestDto
import io.phalconite.domain.dto.UpdateProjectTypeRequestDto
import io.phalconite.services.project.ProjectService
import io.phalconite.services.project.ProjectTypeService
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Route.projectTypeRoutes() {
    val projectTypeService by closestDI().instance<ProjectTypeService>()
    val projectService by closestDI().instance<ProjectService>()

    route("/type") {

        // List
        get("/list") {
            either {
                val projectTypes = projectTypeService.list().bind()

                call.respond(
                    status = HttpStatusCode.OK,
                    ApiDataResource(projectTypes)
                )
            }.mapLeft {
                handleError(it)
            }
        }

        // Find By UID
        get("/find/uid/{uid}") {
            either {
                val uid = call.parameters["uid"] ?: return@either shift(BadRequestError("Invalid Route Parameter!"))

                val projectType = projectTypeService.findByUid(uid).bind()

                call.respond(
                    status = HttpStatusCode.OK,
                    ApiDataResource(projectType)
                )
            }.mapLeft {
                handleError(it)
            }
        }

        post("/create") {
            either {
                val requestBody = runCatching {
                    call.receive<CreateProjectTypeRequestDto>()
                }.getOrElse { return@either shift(UnprocessableEntityError) }

                val response = projectTypeService.create(requestBody).bind()

                call.respond(
                    status = HttpStatusCode.Created,
                    ApiDataResource(response)
                )
            }.mapLeft {
                handleError(it)
            }
        }

        // Update
        patch("/update/{uid}") {
            either {
                val uid = call.parameters["uid"] ?: return@either shift(BadRequestError("Invalid Route Parameter!"))
                val requestBody = runCatching {
                    call.receive<UpdateProjectTypeRequestDto>()
                }.getOrElse { return@either shift(UnprocessableEntityError) }

                val response = projectTypeService.update(uid, requestBody).bind()

                call.respond(
                    status = HttpStatusCode.OK,
                    ApiDataResource(response)
                )
            }.mapLeft {
                handleError(it)
            }
        }

        // Delete
        delete("/delete/{uid}") {
            either {
                val uid = call.parameters["uid"] ?: return@either shift(BadRequestError("Invalid Route Parameter!"))

                val response = projectTypeService.delete(uid).bind()

                call.respond(
                    status = HttpStatusCode.OK,
                    ApiDataResource(response)
                )
            }.mapLeft {
                handleError(it)
            }
        }

        // Relations
        route("/relation/{uid}") {

            // Projects
            get("/projects") {
                either {
                    val uid = call.parameters["uid"] ?: return@either shift(BadRequestError("Invalid Route Parameter!"))

                    val projects = projectService.listByProjectType(uid).bind()

                    call.respond(
                        status = HttpStatusCode.OK,
                        ApiDataResource(projects)
                    )
                }.mapLeft {
                    handleError(it)
                }
            }
        }
    }
}