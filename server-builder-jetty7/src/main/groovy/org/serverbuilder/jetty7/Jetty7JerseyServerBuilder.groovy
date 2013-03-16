package org.serverbuilder.jetty7
import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.WebResource
import com.sun.jersey.spi.container.servlet.ServletContainer
import com.sun.jersey.spi.spring.container.servlet.SpringServlet
import groovy.transform.CompileStatic
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.serverbuilder.JerseyServerBuilder
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.web.context.ContextLoader
import org.springframework.web.context.ContextLoaderListener

import javax.ws.rs.core.Application

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/9/13
 * Time: 6:21 PM
 */
@CompileStatic
class Jetty7JerseyServerBuilder implements JerseyServerBuilder<Server> {
    Class<? extends Application> applicationClass
    String springContextConfigLocation
    ConfigurableApplicationContext springContext
    String contextPath = '/'
    String urlPattern  = '/*'
    int port = 8080

    @Override
    JerseyServerBuilder<Server> withApplicationClass(Class<? extends Application> applicationClass) {
        this.applicationClass = applicationClass
        this
    }

    @Override
    JerseyServerBuilder<Server> withSpringContextConfigLocation(String springContextConfigLocation) {
        this.springContextConfigLocation = springContextConfigLocation
        this
    }

    @Override
    JerseyServerBuilder<Server> withSpringContext(ConfigurableApplicationContext springContext) {
        this.springContext = springContext
        this
    }

    @Override
    JerseyServerBuilder<Server> atContextPath(String contextPath) {
        if (!contextPath.startsWith('/')) {
            throw new IllegalArgumentException("Context path must start with a '/', was $contextPath")
        }
        this.contextPath = contextPath
        this
    }

    @Override
    JerseyServerBuilder<Server> atRootContextPath() {
        this
    }

    @Override
    JerseyServerBuilder<Server> mappedTo(String urlPattern) {
        if (!urlPattern.startsWith('/')) {
            throw new IllegalArgumentException("A servlet url-pattern must start with a '/', was $urlPattern")
        }
        this.urlPattern = urlPattern
        this
    }

    @Override
    JerseyServerBuilder<Server> onPort(int port) {
        this.port = port
        this
    }

    @Override
    WebResource jerseyResource() {
        Client.create().resource("http://localhost:$port")
    }

    @Override
    Server build() {
        def server = new Server(port)
        def handler = new ServletContextHandler()
        def servletHolder
        if (applicationClass) {
            servletHolder = new ServletHolder(new ServletContainer(applicationClass))
        } else if (springContextConfigLocation) {
            handler.addEventListener(new ContextLoaderListener())
            handler.setInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, springContextConfigLocation)
            servletHolder = new ServletHolder(new SpringServlet())
        } else {
            servletHolder = new ServletHolder(new ExistingContextSpringServlet(springContext))
        }
        handler.addServlet(servletHolder, urlPattern)
        handler.contextPath = this.contextPath
        server.handler = handler
        server.start()
        server
    }

    @Override
    void stop(Server server) {
        server.stop()
    }

    static class ExistingContextSpringServlet extends SpringServlet {
        ConfigurableApplicationContext context

        ExistingContextSpringServlet(ConfigurableApplicationContext context) {
            this.context = context
        }

        @Override
        ConfigurableApplicationContext getDefaultContext() {
            context
        }
    }
}
