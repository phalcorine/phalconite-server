package io.phalconite.data

import io.phalconite.config.DatabaseConfig
import io.phalconite.data.entities.*
import io.phalconite.data.seeders.projectSeeders
import io.phalconite.data.seeders.projectTypeSeeders
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

        val tables = listOf(
            UserTable,
            UserAccessTokenTable,
            UserGroupTable,
            UsersToUserGroupsTable,
            ProjectTypeTable,
            ProjectTable,
            ProjectCommentTable
        )
        transaction {
//            SchemaUtils.drop(*tables.toTypedArray())
            println("Creating missing tables and columns")
            SchemaUtils.createMissingTablesAndColumns(*tables.toTypedArray())
        }

        runSeeders()
    }

    private fun runSeeders() {
        projectTypeSeeders()
        userSeeder()
        projectSeeders()
    }
}

suspend fun <T> dbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }