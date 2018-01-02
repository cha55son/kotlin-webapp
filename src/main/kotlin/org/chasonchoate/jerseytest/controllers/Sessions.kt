package org.chasonchoate.jerseytest.controllers

import org.pac4j.jax.rs.annotations.Pac4JCallback
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path

@Path("/sessions")
class Sessions {

    @GET
    @Path("new")
    fun new(): String {
        return """
            <form action="/sessions/create" method="POST">
                <div>
                    <label for="username-input">Username:</label>
                    <input id="username-input" name="username">
                </div>
                <div>
                    <label for="password-input">Password:</label>
                    <input id="password-input" name="password" type="password">
                </div>
                <button type="submit">Login</button>
            </form>
        """
    }

    @POST
    @Path("create")
    @Pac4JCallback()
    fun create() { }
}