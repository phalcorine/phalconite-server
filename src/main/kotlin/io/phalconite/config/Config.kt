package io.phalconite.config

data class AppConfig(
    val database: DatabaseConfig
)

data class DatabaseConfig(
    val jdbcUrl: String,
    val username: String,
    val password: String,
    val driverClassName: String
)