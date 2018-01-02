package org.chasonchoate.jerseytest.auth

import org.chasonchoate.jerseytest.models.User
import org.chasonchoate.jerseytest.models.UserTable
import org.jetbrains.exposed.sql.transactions.transaction
import org.pac4j.core.context.Pac4jConstants
import org.pac4j.core.context.WebContext
import org.pac4j.core.credentials.UsernamePasswordCredentials
import org.pac4j.core.credentials.authenticator.Authenticator
import org.pac4j.core.exception.CredentialsException
import org.pac4j.core.profile.CommonProfile

class SimpleAuthenticator : Authenticator<UsernamePasswordCredentials> {
    override fun validate(credentials: UsernamePasswordCredentials, context: WebContext) {
        transaction {
            val users = User.find { UserTable.username eq credentials.username }
            if (users.empty()) {
                // TODO: Change to general error
                throw CredentialsException("Invalid username")
            }
            val user = users.elementAt(0)

            if (!user.matchesPassword(credentials.password)) {
                throw CredentialsException("Invalid credentials")
            }

            val profile = CommonProfile()
            with(profile) {
                setId(user.id)
                addAttribute(Pac4jConstants.USERNAME, user.username)
            }
            credentials.userProfile = profile
        }
    }
}