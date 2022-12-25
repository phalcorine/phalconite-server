package io.phalconite.plugins

import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.http.content.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.phalconite.api.GenericErrorResponse
import io.phalconite.api.authRoutes
import io.phalconite.api.project.moduleProjectRoutes
import io.phalconite.api.user.moduleUserRoutes
import io.phalconite.infra.AppException
import io.phalconite.infra.UnauthorizedException

fun Application.configureRouting() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
        }
        exception<AppException> { call, cause ->
            when (cause) {
                is UnauthorizedException -> {
                    call.respond(
                        status = HttpStatusCode.Unauthorized,
                        GenericErrorResponse(cause.message)
                    )
                }
            }
        }
    }
    

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        route("/api") {
            authRoutes()
            moduleProjectRoutes()
            moduleUserRoutes()
        }
        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
    }
}
