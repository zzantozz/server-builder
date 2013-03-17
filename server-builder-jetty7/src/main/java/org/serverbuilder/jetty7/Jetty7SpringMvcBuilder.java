package org.serverbuilder.jetty7;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import groovy.transform.CompileStatic;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.serverbuilder.SpringMvcServerBuilder;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@CompileStatic
public class Jetty7SpringMvcBuilder implements SpringMvcServerBuilder<Server> {
    private WebApplicationContext rootContext;
    private WebApplicationContext dispatcherContext;
    private String contextPath = "/";
    private String urlPattern = "/*";
    private int port = 8080;

    @Override
    public SpringMvcServerBuilder<Server> withRootContext(WebApplicationContext rootContext) {
        this.rootContext = rootContext;
        return this;
    }

    @Override
    public SpringMvcServerBuilder<Server> withDispatcherContext(WebApplicationContext dispatcherContext) {
        this.dispatcherContext = dispatcherContext;
        return this;
    }

    @Override
    public SpringMvcServerBuilder<Server> atContextPath(String contextPath) {
        if (!contextPath.startsWith("/")) {
            throw new IllegalArgumentException("Context path must start with a '/', was " + contextPath);
        }

        this.contextPath = contextPath;
        return this;
    }

    @Override
    public SpringMvcServerBuilder<Server> atRootContextPath() {
        return this;
    }

    @Override
    public SpringMvcServerBuilder<Server> mappedTo(String urlPattern) {
        if (!urlPattern.startsWith("/")) {
            throw new IllegalArgumentException("A servlet url-pattern must start with a '/', was " + urlPattern);
        }

        this.urlPattern = urlPattern;
        return this;
    }

    @Override
    public SpringMvcServerBuilder<Server> onPort(int port) {
        this.port = port;
        return this;
    }

    @Override
    public WebResource jerseyResource() {
        return Client.create().resource("http://localhost:" + port);
    }

    @Override
    public Server build() {
        ServletContextHandler handler = new ServletContextHandler();
        if (DefaultGroovyMethods.asBoolean(rootContext))
            handler.addEventListener(new ContextLoaderListener(rootContext));
        ServletHolder servletHolder = new ServletHolder(new DispatcherServlet(dispatcherContext));
        handler.setContextPath(this.contextPath);
        handler.addServlet(servletHolder, this.urlPattern);
        Server server = new Server(this.port);
        server.setHandler(handler);
        try {
            server.start();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to start server", e);
        }
        return server;
    }

    @Override
    public void stop(Server server) {
        try {
            server.stop();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to stop server", e);
        }
    }
}
