package org.chasonchoate.jerseytest

import org.chasonchoate.jerseytest.auth.SimpleTestAuthenticator
import org.chasonchoate.jerseytest.controllers.Books
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.servlet.ServletContainer
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.ThreadLocalTransactionManager
import org.pac4j.core.client.Clients
import org.pac4j.http.client.indirect.IndirectBasicAuthClient
import org.pac4j.jax.rs.features.JaxRsConfigProvider
import org.pac4j.jax.rs.features.Pac4JSecurityFeature
import org.pac4j.jax.rs.pac4j.JaxRsConfig
import org.pac4j.jax.rs.pac4j.JaxRsUrlResolver
import org.pac4j.jax.rs.servlet.features.ServletJaxRsContextFactoryProvider
import java.sql.Connection
import java.util.*

class App { }

fun registerControllers(config: ResourceConfig) {
    /**
     * Add additional controllers here
     */
    config.register(Books())
}

fun registerAuthentication(config: ResourceConfig) {
    val basicAuthClient = IndirectBasicAuthClient(SimpleTestAuthenticator())

    val clients = Clients("notUsedCallbackUrl", listOf(basicAuthClient))
    clients.urlResolver = JaxRsUrlResolver()
    val authConfig = JaxRsConfig()
    authConfig.clients = clients

    config
        .register(JaxRsConfigProvider(authConfig))
        .register(ServletJaxRsContextFactoryProvider())
        .register(Pac4JSecurityFeature())
}

fun createDatabaseConnection() {
    val prop = Properties()
    App::class.java.getResourceAsStream("/database.properties").use {
        prop.load(it)
    }
    Database.connect(prop["database.url"] as String,
            driver = prop["database.driver"] as String,
            manager = { ThreadLocalTransactionManager(it, Connection.TRANSACTION_SERIALIZABLE) })
}

fun main(args: Array<String>) {
    val server = Server(3000)
    val context = ServletContextHandler(ServletContextHandler.SESSIONS)
    context.contextPath = "/"
    server.handler = context

    val config = ResourceConfig()
    createDatabaseConnection()
    registerAuthentication(config)
    registerControllers(config)

    context.addServlet(ServletHolder(ServletContainer(config)), "/*")

    try {
        server.start()
        server.join()
    } catch (e: Exception) {
        println("Error occurred during startup: ${e.message}")
        server.stop()
        server.destroy()
    }
}
