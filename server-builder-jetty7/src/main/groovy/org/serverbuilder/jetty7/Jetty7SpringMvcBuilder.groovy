package org.serverbuilder.jetty7

import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.WebResource
import groovy.transform.CompileStatic
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.serverbuilder.SpringMvcServerBuilder
import org.springframework.web.context.ContextLoaderListener
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.servlet.DispatcherServlet

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/9/13
 * Time: 11:04 AM
 */
@CompileStatic
class Jetty7SpringMvcBuilder implements SpringMvcServerBuilder<Server> {
    private WebApplicationContext rootContext
    private WebApplicationContext dispatcherContext
    private String contextPath = '/'
    private String urlPattern = '/*'
    private int port = 8080

    @Override
    SpringMvcServerBuilder<Server> withRootContext(WebApplicationContext rootContext) {
        this.rootContext = rootContext
        this
    }

    @Override
    SpringMvcServerBuilder<Server> withDispatcherContext(WebApplicationContext dispatcherContext) {
        this.dispatcherContext = dispatcherContext
        this
    }

    @Override
    SpringMvcServerBuilder<Server> atContextPath(String contextPath) {
        if (!contextPath.startsWith('/')) {
            throw new IllegalArgumentException("Context path must start with a '/', was $contextPath")
        }
        this.contextPath = contextPath
        this
    }

    @Override
    SpringMvcServerBuilder<Server> atRootContextPath() {
        this
    }

    @Override
    SpringMvcServerBuilder<Server> mappedTo(String urlPattern) {
        if (!urlPattern.startsWith('/')) {
            throw new IllegalArgumentException("A servlet url-pattern must start with a '/', was $urlPattern")
        }
        this.urlPattern = urlPattern
        this
    }

    @Override
    SpringMvcServerBuilder<Server> onPort(int port) {
        this.port = port
        this
    }

    @Override
    WebResource jerseyResource() {
        Client.create().resource("http://localhost:$port")
    }

    @Override
    Server build() {
        def handler = new ServletContextHandler()
        if (rootContext) handler.addEventListener(new ContextLoaderListener(rootContext))
        def servletHolder = new ServletHolder(new DispatcherServlet(dispatcherContext))
        handler.contextPath = this.contextPath
        handler.addServlet(servletHolder, this.urlPattern)
        Server server = new Server(this.port)
        server.handler = handler
        server.start()
        server
    }

    @Override
    void stop(Server server) {
        server.stop()
    }
}
