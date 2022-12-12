package io.phalconite.data

import io.phalconite.config.DatabaseConfig
import io.phalconite.data.entities.*
import io.phalconite.data.seeders.userSeeder
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(databaseConfig: DatabaseConfig) {
        Database.connect(
            url = databaseConfig.jdbcUrl,
            user = databaseConfig.username,
            password = databaseConfig.password,
            driver = databaseConfig.driverClassName
        )

        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                UserTable,
                UserAccessTokenTable,
                ProjectStatusTable,
                ProjectTypeTable,
                ProjectTable
            )
        }

        runSeeders()
    }

    private fun runSeeders() {
        userSeeder()
    }
}

suspend fun <T> dbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }