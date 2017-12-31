package org.chasonchoate.jerseytest.auth

import org.pac4j.core.context.WebContext
import org.pac4j.core.credentials.UsernamePasswordCredentials
import org.pac4j.core.credentials.authenticator.Authenticator
import org.pac4j.core.exception.CredentialsException

class SimpleTestAuthenticator : Authenticator<UsernamePasswordCredentials> {
    override fun validate(credentials: UsernamePasswordCredentials, context: WebContext) {
        throw CredentialsException("Invalid credentials")
    }
}