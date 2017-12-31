package org.chasonchoate.jerseytest.controllers

import javax.ws.rs.GET
import javax.ws.rs.Path

@Path("/")
class Root {
    @GET
    fun index(): String {
       return "Root path"
    }
}