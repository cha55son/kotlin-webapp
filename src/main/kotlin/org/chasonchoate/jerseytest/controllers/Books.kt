package org.chasonchoate.jerseytest.controllers

import org.pac4j.core.profile.CommonProfile
import org.pac4j.jax.rs.annotations.Pac4JProfile
import org.pac4j.jax.rs.annotations.Pac4JSecurity
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/books")
@Pac4JSecurity()
class Books {
    var counter = 0

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun books(@Pac4JProfile profile: CommonProfile): String {
        counter++
        return "Some books here ${counter} change - (${profile.id} | ${profile.username})"
    }
}