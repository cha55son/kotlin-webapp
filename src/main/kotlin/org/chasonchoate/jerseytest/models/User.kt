package org.chasonchoate.jerseytest.models

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object UserTable: IntIdTable() {
    val username = varchar("username", 255)
}

class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(UserTable)

    var username by UserTable.username
}
