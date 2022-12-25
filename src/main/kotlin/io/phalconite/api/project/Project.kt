package io.phalconite.api.project

import arrow.core.continuations.either
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.phalconite.api.ApiDataResource
import io.phalconite.api.AuthLoggedInUserDto
import io.phalconite.api.handleError
import io.phalconite.domain.AuthInvalidCredentials
import io.phalconite.domain.BadRequestError
import io.phalconite.domain.UnprocessableEntityError
import io.phalconite.domain.dto.CreateProjectCommentRequestDto
import io.phalconite.domain.dto.CreateProjectRequestDto
import io.phalconite.domain.dto.UpdateProjectCommentRequestDto
import io.phalconite.domain.dto.UpdateProjectRequestDto
import io.phalconite.services.project.ProjectCommentService
import io.phalconite.services.project.ProjectService
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Route.projectRoutes() {
    val projectService by closestDI().instance<ProjectService>()
    val projectCommentService by closestDI().instance<ProjectCommentService>()

    route("/projects") {
        // List
        get("/list") {
            either {
                val projects = projectService.list().bind()

                call.respond(
                    status = HttpStatusCode.OK,
                    ApiDataResource(projects)
                )
            }.mapLeft {
                handleError(it)
            }
        }

        get("/find/uid/{uid}") {
            either {
                val uid = call.parameters["uid"] ?: return@either shift(BadRequestError("Invalid Route Parameter!"))

                val project = projectService.findByUid(uid).bind()

                call.respond(
                    status = HttpStatusCode.OK,
                    ApiDataResource(project)
                )
            }.mapLeft {
                handleError(it)
            }
        }

        post("/create") {
            either {
                val requestBody = runCatching {
                    call.receive<CreateProjectRequestDto>()
                }.getOrElse {
                    return@either shift(UnprocessableEntityError)
                }

                val user = runCatching {
                    call.principal<AuthLoggedInUserDto>()
                }.getOrElse {
                    return@either shift(AuthInvalidCredentials)
                }

                val response = projectService.create(requestBody, user!!.uid).bind()

                call.respond(
                    status = HttpStatusCode.Created,
                    ApiDataResource(response)
                )
            }.mapLeft {
                handleError(it)
            }
        }

        patch("/update/{uid}") {
            either {
                val uid = call.parameters["uid"] ?: return@either shift(BadRequestError("Invalid Route Parameter!"))
                val requestBody = runCatching {
                    call.receive<UpdateProjectRequestDto>()
                }.getOrElse { return@either shift(UnprocessableEntityError) }

                val response = projectService.update(uid, requestBody).bind()

                call.respond(
                    status = HttpStatusCode.OK,
                    ApiDataResource(response)
                )
            }.mapLeft {
                handleError(it)
            }
        }

        delete("/delete/{uid}") {
            either {
                val uid = call.parameters["uid"] ?: return@either shift(BadRequestError("Invalid Route Parameter!"))

                val response = projectService.delete(uid).bind()

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

            // Comments
            route("/comments") {
                // List
                get("/list") {
                    either {
                        val projectUid = call.parameters["uid"] ?: return@either shift(BadRequestError("Invalid Route Parameter!"))

                        val comments = projectCommentService.listCommentsByProject(projectUid).bind()

                        call.respond(
                            status = HttpStatusCode.OK,
                            ApiDataResource(comments)
                        )
                    }.mapLeft {
                        handleError(it)
                    }
                }

                post("/create") {
                    either {
                        val projectUid = call.parameters["uid"] ?: return@either shift(BadRequestError("Invalid Route Parameter!"))

                        val requestBody = runCatching {
                            call.receive<CreateProjectCommentRequestDto>()
                        }.getOrElse { return@either shift(UnprocessableEntityError) }

                        val user = runCatching {
                            call.principal<AuthLoggedInUserDto>()
                        }.getOrElse {
                            return@either shift(AuthInvalidCredentials)
                        }

                        val response = projectCommentService.create(requestBody, projectUid, authorUid = user!!.uid).bind()

                        call.respond(
                            status = HttpStatusCode.Created,
                            ApiDataResource(response)
                        )
                    }.mapLeft {
                        handleError(it)
                    }
                }

                patch("/update/{comment_uid}") {
                    either {
                        val projectUid = call.parameters["uid"] ?: return@either shift(BadRequestError("Invalid Route Parameter!"))
                        val commentUid = call.parameters["comment_uid"] ?: return@either shift(BadRequestError("Invalid Route Parameter!"))

                        val requestBody = runCatching {
                            call.receive<UpdateProjectCommentRequestDto>()
                        }.getOrElse { return@either shift(UnprocessableEntityError) }

                        val user = runCatching {
                            call.principal<AuthLoggedInUserDto>()
                        }.getOrElse {
                            return@either shift(AuthInvalidCredentials)
                        }

                        val response = projectCommentService.update(
                            uid = commentUid,
                            data = requestBody,
                            projectUid = projectUid,
                            authorUid = user!!.uid
                        ).bind()

                        call.respond(
                            status = HttpStatusCode.OK,
                            ApiDataResource(response)
                        )
                    }.mapLeft {
                        handleError(it)
                    }
                }

                delete("/delete/{comment_uid}") {
                    either {
                        val projectUid = call.parameters["uid"] ?: return@either shift(BadRequestError("Invalid Route Parameter!"))
                        val commentUid = call.parameters["comment_uid"] ?: return@either shift(BadRequestError("Invalid Route Parameter!"))

                        val user = runCatching {
                            call.principal<AuthLoggedInUserDto>()
                        }.getOrElse {
                            return@either shift(AuthInvalidCredentials)
                        }

                        val response = projectCommentService.delete(
                            uid = commentUid,
                            projectUid = projectUid,
                            authorUid = user!!.uid
                        ).bind()

                        call.respond(
                            status = HttpStatusCode.OK,
                            ApiDataResource(response)
                        )
                    }.mapLeft {
                        handleError(it)
                    }
                }
            }

            //
        }
    }
}