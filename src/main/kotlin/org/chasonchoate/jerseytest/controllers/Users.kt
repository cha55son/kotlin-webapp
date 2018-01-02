package org.chasonchoate.jerseytest.controllers

import org.chasonchoate.jerseytest.models.User
import org.jetbrains.exposed.sql.transactions.transaction
import java.net.URI
import javax.ws.rs.FormParam
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.core.Response

@Path("/users")
class Users {

    @GET
    @Path("new")
    fun new(): String {
        return """
            <form action="/users/create" method="POST">
                <div>
                    <label for="username-input">Username:</label>
                    <input id="username-input" name="username">
                </div>
                <div>
                    <label for="email-input">Email:</label>
                    <input id="email-input" name="email">
                </div>
                <div>
                    <label for="password-input">Password:</label>
                    <input id="password-input" name="password" type="password">
                </div>
                <button type="submit">Create User</button>
            </form>
        """
    }

    @POST
    @Path("create")
    fun create(
        @FormParam("username") aUsername: String?,
        @FormParam("email") anEmail: String?,
        @FormParam("password") aPassword: String?
    ): Response {
        if (aUsername == null || anEmail == null || aPassword == null) {
            return Response.temporaryRedirect(URI("/sessions/new")).build()
        }
        transaction {
            User.new {
                username = aUsername
                email = anEmail
                setPassword(aPassword)
            }
        }
        // return Response.created(URI("/sessions/new")).build()
        return Response.temporaryRedirect(URI("/sessions/new")).build()
    }
}