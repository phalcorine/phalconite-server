package io.phalconite.data.seeders

import io.github.thibaultmeyer.cuid.CUID
import io.phalconite.data.entities.UserTable
import io.phalconite.util.BcryptService
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.UUID

fun userSeeder() {
    transaction {
        val userCount = UserTable.selectAll().count()
        if (userCount > 0) {
            return@transaction
        }

        UserTable.insert {
            val timeNow = LocalDateTime.now()
            it[uid] = CUID.randomCUID().toString()
            it[full_name] = "Andromadus Naruto"
            it[email] = "andromadusv2@gmail.com"
            it[password_hash] = BcryptService.hash("meowmeow")
            it[active] = true
            it[created_at] = timeNow
            it[updated_at] = timeNow
        }
    }
}