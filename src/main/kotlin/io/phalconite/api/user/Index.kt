package io.phalconite.api.user

import io.ktor.server.routing.*

fun Route.moduleUserRoutes() {
    route("/user") {
        userRoutes()
        userGroupRoutes()
    }
}

// Services



