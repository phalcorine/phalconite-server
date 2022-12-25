package io.phalconite.domain.enums

import kotlinx.serialization.Serializable

@Serializable
enum class ProjectStatusEnum(val value: String) {
    DRAFT("draft"),
    ONGOING("ongoing"),
    COMPLETED("completed"),
    ARCHIVED("archived")
}