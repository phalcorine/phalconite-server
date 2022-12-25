package io.phalconite.api.project

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.phalconite.plugins.CLIENT_AUTH_NAME

fun Route.moduleProjectRoutes() {
    authenticate(CLIENT_AUTH_NAME) {
        route("/project") {
            projectTypeRoutes()
            projectRoutes()
        }
    }
}