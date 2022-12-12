package io.phalconite.plugins

import io.ktor.server.application.*
import io.phalconite.config.AppConfig
import io.phalconite.config.DatabaseConfig

fun Application.loadConfig(): AppConfig {
    val jdbcUrl = environment.config.property("database.jdbcUrl").getString()
    val dbUsername = environment.config.property("database.username").getString()
    val dbPassword = environment.config.property("database.password").getString()
    val driverClassName = environment.config.property("database.driverClassName").getString()

    return AppConfig(
        database = DatabaseConfig(
            jdbcUrl = jdbcUrl,
            username = dbUsername,
            password = dbPassword,
            driverClassName = driverClassName
        )
    )
}