package org.serverbuilder.jetty7;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import groovy.transform.CompileStatic;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.serverbuilder.JerseyServerBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;

import javax.ws.rs.core.Application;

@CompileStatic
public class Jetty7JerseyServerBuilder implements JerseyServerBuilder<Server> {
    private Class<? extends Application> applicationClass;
    private String springContextConfigLocation;
    private ConfigurableApplicationContext springContext;
    private String contextPath = "/";
    private String urlPattern = "/*";
    private int port = 8080;

    @Override
    public JerseyServerBuilder<Server> withApplicationClass(Class<? extends Application> applicationClass) {
        this.applicationClass = applicationClass;
        return this;
    }

    @Override
    public JerseyServerBuilder<Server> withSpringContextConfigLocation(String springContextConfigLocation) {
        this.springContextConfigLocation = springContextConfigLocation;
        return this;
    }

    @Override
    public JerseyServerBuilder<Server> withSpringContext(ConfigurableApplicationContext springContext) {
        this.springContext = springContext;
        return this;
    }

    @Override
    public JerseyServerBuilder<Server> atContextPath(String contextPath) {
        if (!contextPath.startsWith("/")) {
            throw new IllegalArgumentException("Context path must start with a '/', was " + contextPath);
        }

        this.contextPath = contextPath;
        return this;
    }

    @Override
    public JerseyServerBuilder<Server> atRootContextPath() {
        return this;
    }

    @Override
    public JerseyServerBuilder<Server> mappedTo(String urlPattern) {
        if (!urlPattern.startsWith("/")) {
            throw new IllegalArgumentException("A servlet url-pattern must start with a '/', was " + urlPattern);
        }

        this.urlPattern = urlPattern;
        return this;
    }

    @Override
    public JerseyServerBuilder<Server> onPort(int port) {
        this.port = port;
        return this;
    }

    @Override
    public WebResource jerseyResource() {
        return Client.create().resource("http://localhost:" + port);
    }

    @Override
    public Server build() {
        Server server = new Server(port);
        ServletContextHandler handler = new ServletContextHandler();
        Object servletHolder;
        if (DefaultGroovyMethods.asBoolean(applicationClass)) {
            servletHolder = new ServletHolder(new ServletContainer(applicationClass));
        } else if (springContextConfigLocation != null) {
            handler.addEventListener(new ContextLoaderListener());
            handler.setInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, springContextConfigLocation);
            servletHolder = new ServletHolder(new SpringServlet());
        } else {
            servletHolder = new ServletHolder(new ExistingContextSpringServlet(springContext));
        }

        handler.addServlet((ServletHolder) servletHolder, urlPattern);
        handler.setContextPath(this.contextPath);
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

    public static class ExistingContextSpringServlet extends SpringServlet {
        private ConfigurableApplicationContext context;

        public ExistingContextSpringServlet(ConfigurableApplicationContext context) {
            this.context = context;
        }

        @Override
        public ConfigurableApplicationContext getDefaultContext() {
            return context;
        }
    }
}
