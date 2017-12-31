package org.chasonchoate.jerseytest.controllers

import org.pac4j.core.context.DefaultAuthorizers
import org.pac4j.jax.rs.annotations.Pac4JSecurity
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/books")
class Books {
    var counter = 0

    @GET
    @Pac4JSecurity(authorizers = [DefaultAuthorizers.IS_AUTHENTICATED])
    @Produces(MediaType.TEXT_PLAIN)
    fun books(): String {
        counter++
        return "Some books here ${counter} change"
    }
}