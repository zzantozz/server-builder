package org.serverbuilder.jetty8;

import com.sun.jersey.spi.container.servlet.ServletContainer;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.serverbuilder.JerseyServerBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;

import javax.ws.rs.core.Application;

public class Jetty8JerseyServerBuilder extends SimpleServletBasedJettyServerBuilder<JerseyServerBuilder<Server>> implements JerseyServerBuilder<Server> {
    private Class<? extends Application> applicationClass;
    private String springContextConfigLocation;
    private ConfigurableApplicationContext springContext;

    public Jetty8JerseyServerBuilder() {
    }

    public Jetty8JerseyServerBuilder(int port, String contextPath, String urlPattern, Class<? extends Application> applicationClass, String springContextConfigLocation, ConfigurableApplicationContext springContext) {
        super(port, contextPath, urlPattern);
        this.applicationClass = applicationClass;
        this.springContextConfigLocation = springContextConfigLocation;
        this.springContext = springContext;
    }

    public Jetty8JerseyServerBuilder withApplicationClass(Class<? extends Application> applicationClass) {
        return new Jetty8JerseyServerBuilder(getPort(), getContextPath(), getUrlPattern(), applicationClass, springContextConfigLocation, springContext);
    }

    public Jetty8JerseyServerBuilder withSpringContextConfigLocation(String springContextConfigLocation) {
        return new Jetty8JerseyServerBuilder(getPort(), getContextPath(), getUrlPattern(), applicationClass, springContextConfigLocation, springContext);
    }

    public Jetty8JerseyServerBuilder withSpringContext(ConfigurableApplicationContext springContext) {
        return new Jetty8JerseyServerBuilder(getPort(), getContextPath(), getUrlPattern(), applicationClass, springContextConfigLocation, springContext);
    }

    public Jetty8JerseyServerBuilder atContextPath(String contextPath) {
        if (!contextPath.startsWith("/")) {
            throw new IllegalArgumentException("Context path must start with a '/', was " + contextPath);
        }

        return new Jetty8JerseyServerBuilder(getPort(), contextPath, getUrlPattern(), applicationClass, springContextConfigLocation, springContext);
    }

    public Jetty8JerseyServerBuilder atRootContextPath() {
        String contextPath = "/";
        return new Jetty8JerseyServerBuilder(getPort(), contextPath, getUrlPattern(), applicationClass, springContextConfigLocation, springContext);
    }

    public Jetty8JerseyServerBuilder onPort(int port) {
        return new Jetty8JerseyServerBuilder(port, getContextPath(), getUrlPattern(), applicationClass, springContextConfigLocation, springContext);
    }

    public Jetty8JerseyServerBuilder mappedTo(String urlPattern) {
        if (!urlPattern.startsWith("/")) {
            throw new IllegalArgumentException("A servlet url-pattern must start with a '/', was " + urlPattern);
        }

        return new Jetty8JerseyServerBuilder(getPort(), getContextPath(), urlPattern, applicationClass, springContextConfigLocation, springContext);
    }

    @Override
    public ServletHolder servletHolder(ServletContextHandler handler) {
        if (applicationClass != null) {
            return new ServletHolder(new ServletContainer(applicationClass));
        } else if (springContextConfigLocation != null) {
            handler.addEventListener(new ContextLoaderListener());
            handler.setInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, springContextConfigLocation);
            return new ServletHolder(new SpringServlet());
        } else {
            return new ServletHolder(new ExistingContextSpringServlet(springContext));
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
