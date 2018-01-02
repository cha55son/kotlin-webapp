package org.chasonchoate.jerseytest.models

import org.apache.shiro.authc.credential.DefaultPasswordService
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import org.pac4j.core.credentials.password.PasswordEncoder
import org.pac4j.core.credentials.password.ShiroPasswordEncoder

object UserTable: IntIdTable("users") {
    val username = varchar("username", 255)
    val email = varchar("email", 255)
    val encryptedPassword = varchar("encrypted_password", 255)
}

class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(UserTable)

    var username by UserTable.username
    var email by UserTable.email
    var encryptedPassword by UserTable.encryptedPassword

    fun setPassword(password: String) {
        encryptedPassword = getEncoder().encode(password)
    }

    fun matchesPassword(password: String): Boolean {
        return getEncoder().matches(password, encryptedPassword)
    }

    private fun getEncoder(): PasswordEncoder {
        return ShiroPasswordEncoder(DefaultPasswordService())
    }
}
