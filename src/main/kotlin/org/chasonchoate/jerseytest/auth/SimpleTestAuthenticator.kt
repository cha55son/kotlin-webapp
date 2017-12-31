package org.chasonchoate.jerseytest.auth

import org.chasonchoate.jerseytest.models.User
import org.chasonchoate.jerseytest.models.UserTable
import org.pac4j.core.context.WebContext
import org.pac4j.core.credentials.UsernamePasswordCredentials
import org.pac4j.core.credentials.authenticator.Authenticator
import org.pac4j.core.exception.CredentialsException

class SimpleTestAuthenticator : Authenticator<UsernamePasswordCredentials> {
    override fun validate(credentials: UsernamePasswordCredentials, context: WebContext) {
        println("testing")
        val users = User.find { UserTable.username eq credentials.username }
        if (users.empty()) {
            throw CredentialsException("Invalid credentials")
        }
    }
}