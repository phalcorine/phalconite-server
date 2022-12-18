package io.phalconite.data.entities

import org.jetbrains.exposed.sql.Table

object UsersToUserGroupsTable : Table("users_to_user_groups") {
    val user_uid = reference("user_id", UserTable.uid)
    val user_group_uid = reference("user_group_uid", UserGroupTable.uid)

    override val primaryKey = PrimaryKey(user_uid, user_group_uid)
}