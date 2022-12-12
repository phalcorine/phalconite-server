package io.phalconite

import io.ktor.server.application.*
import io.phalconite.data.DatabaseFactory
import io.phalconite.plugins.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    val config = loadConfig()
    DatabaseFactory.init(config.database)
    configureDI()
    configureSockets()
    configureSerialization()
    configureTemplating()
    configureMonitoring()
    configureHTTP()
    configureSecurity()
    configureRouting()
}
