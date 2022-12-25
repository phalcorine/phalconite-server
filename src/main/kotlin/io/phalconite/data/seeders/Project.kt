package io.phalconite.data.seeders

import io.github.thibaultmeyer.cuid.CUID
import io.phalconite.data.entities.ProjectTable
import io.phalconite.data.entities.ProjectTypeTable
import io.phalconite.domain.dto.CreateProjectTypeDto
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

fun projectTypeSeeders() {
    transaction {
        val projectTypeCount = ProjectTypeTable.selectAll().count()
        if (projectTypeCount > 0) {
            return@transaction
        }

        val projectTypes = listOf(
            CreateProjectTypeDto(
                uid = CUID.randomCUID().toString(),
                name = "Government Projects",
                description = "This is a container for government-related projects..."
            ),
            CreateProjectTypeDto(
                uid = CUID.randomCUID().toString(),
                name = "NGO Projects",
                description = "This is a container for NGO projects..."
            ),
            CreateProjectTypeDto(
                uid = CUID.randomCUID().toString(),
                name = "In-House Projects",
                description = "This is a container for in-house or internal projects..."
            ),
        )

        ProjectTypeTable.batchInsert(projectTypes) { projectType ->
            val now = LocalDateTime.now()
            this[ProjectTypeTable.uid] = projectType.uid
            this[ProjectTypeTable.name] = projectType.name
            this[ProjectTypeTable.description] = projectType.description
            this[ProjectTypeTable.created_at] = now
            this[ProjectTypeTable.updated_at] = now
        }
    }
}

/**
 * DependsOn: [projectTypeSeeders], [userSeeder]
 */
fun projectSeeders() {
    transaction {
        val projectCount = ProjectTable.selectAll().count()
        if (projectCount > 0) {
            return@transaction
        }

        // Set up records
    }
}