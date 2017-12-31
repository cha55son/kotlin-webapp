package org.chasonchoate.jerseytest

import org.chasonchoate.jerseytest.controllers.Books
import org.chasonchoate.jerseytest.controllers.Root
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.servlet.ServletContainer
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.ThreadLocalTransactionManager
import org.pac4j.core.client.Clients
import org.pac4j.http.client.direct.DirectBasicAuthClient
import org.pac4j.jax.rs.features.JaxRsConfigProvider
import org.pac4j.jax.rs.features.Pac4JSecurityFeature
import org.pac4j.jax.rs.pac4j.JaxRsConfig
import org.pac4j.jax.rs.pac4j.JaxRsUrlResolver
import org.pac4j.jax.rs.servlet.features.ServletJaxRsContextFactoryProvider
import java.sql.Connection
import java.util.*
import org.pac4j.http.credentials.authenticator.test.SimpleTestUsernamePasswordAuthenticator
import org.pac4j.http.client.direct.DirectFormClient
import org.slf4j.LoggerFactory


class App {
    companion object {
        val log = LoggerFactory.getLogger(this::class.java)

        private fun registerControllers(config: ResourceConfig) {
            /**
             * Add additional controllers here
             */
            log.debug("Registering controllers...")
            config.register(Root())
            config.register(Books())
            log.debug("Finished registering controllers")
        }

        private fun registerAuthentication(config: ResourceConfig) {
            log.debug("Registering authentication...")
            val auth = SimpleTestUsernamePasswordAuthenticator()
            val formClient = DirectFormClient(auth)
            val basicAuthClient = DirectBasicAuthClient(auth)

            val clients = Clients("notUsedCallbackUrl", listOf(basicAuthClient, formClient))
            clients.urlResolver = JaxRsUrlResolver()
            val authConfig = JaxRsConfig()
            authConfig.clients = clients
            authConfig.defaultClients = "DirectBasicAuthClient"

            config
                .register(JaxRsConfigProvider(authConfig))
                .register(ServletJaxRsContextFactoryProvider())
                .register(Pac4JSecurityFeature())
            log.debug("Finished registering authentication")
        }

        private fun createDatabaseConnection(prop: Properties) {
            Database.connect(prop["database.url"] as String,
                    driver = prop["database.driver"] as String,
                    manager = { ThreadLocalTransactionManager(it, Connection.TRANSACTION_SERIALIZABLE) })
            log.debug("Created database connection")
        }

        @JvmStatic fun main(args: Array<String>) {
            val prop = Properties()
            App::class.java.getResourceAsStream("/server.properties").use {
                prop.load(it)
            }

            val server = Server(Integer.parseInt(prop["server.port"] as String))
            val context = ServletContextHandler(ServletContextHandler.SESSIONS)
            context.contextPath = prop["server.path"] as String
            server.handler = context

            val config = ResourceConfig()
            createDatabaseConnection(prop)
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
    }
}

