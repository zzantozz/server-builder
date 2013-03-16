package org.serverbuilder.jetty8

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
class Jetty8JerseyServerBuilder extends SimpleServletBasedJettyServerBuilder<Jetty8JerseyServerBuilder> implements JerseyServerBuilder<Server> {
    final Class<? extends Application> applicationClass
    final String springContextConfigLocation
    final ConfigurableApplicationContext springContext

    Jetty8JerseyServerBuilder(Class<? extends Application> applicationClass) {
        this.applicationClass = applicationClass
    }

    Jetty8JerseyServerBuilder(String springContextConfigLocation) {
        this.springContextConfigLocation = springContextConfigLocation
    }

    Jetty8JerseyServerBuilder(ConfigurableApplicationContext springContext) {
        this.springContext = springContext
    }

    Jetty8JerseyServerBuilder(int port, String contextPath, String urlPattern, Class<? extends Application> applicationClass, String springContextConfigLocation, ConfigurableApplicationContext springContext) {
        super(port, contextPath, urlPattern)
        this.applicationClass = applicationClass
        this.springContextConfigLocation = springContextConfigLocation
        this.springContext = springContext
    }

    static Jetty8JerseyServerBuilder newJerseyServer(Class<? extends Application> applicationClass) {
        new Jetty8JerseyServerBuilder(applicationClass)
    }

    static Jetty8JerseyServerBuilder newJerseyServer(String springContextConfigLocation) {
        new Jetty8JerseyServerBuilder(springContextConfigLocation)
    }

    static Jetty8JerseyServerBuilder newJerseyServer(ConfigurableApplicationContext springContext) {
        new Jetty8JerseyServerBuilder(springContext)
    }

    Jetty8JerseyServerBuilder withApplicationClass(Class<? extends Application> applicationClass) {
        new Jetty8JerseyServerBuilder(port, contextPath, urlPattern, applicationClass, springContextConfigLocation, springContext)
    }

    Jetty8JerseyServerBuilder withSpringContextConfigLocation(String springContextConfigLocation) {
        new Jetty8JerseyServerBuilder(port, contextPath, urlPattern, applicationClass, springContextConfigLocation, springContext)
    }

    Jetty8JerseyServerBuilder withSpringContext(ConfigurableApplicationContext springContext) {
        new Jetty8JerseyServerBuilder(port, contextPath, urlPattern, applicationClass, springContextConfigLocation, springContext)
    }

    Jetty8JerseyServerBuilder atContextPath(String contextPath) {
        if (!contextPath.startsWith('/')) {
            throw new IllegalArgumentException("Context path must start with a '/', was $contextPath")
        }
        new Jetty8JerseyServerBuilder(port, contextPath, urlPattern, applicationClass, springContextConfigLocation, springContext)
    }

    Jetty8JerseyServerBuilder atRootContextPath() {
        def contextPath = '/'
        new Jetty8JerseyServerBuilder(port, contextPath, urlPattern, applicationClass, springContextConfigLocation, springContext)
    }

    Jetty8JerseyServerBuilder onPort(int port) {
        new Jetty8JerseyServerBuilder(port, contextPath, urlPattern, applicationClass, springContextConfigLocation, springContext)
    }

    Jetty8JerseyServerBuilder mappedTo(String urlPattern) {
        if (!urlPattern.startsWith('/')) {
            throw new IllegalArgumentException("A servlet mapping must start with a '/', was $urlPattern")
        }
        new Jetty8JerseyServerBuilder(port, contextPath, urlPattern, applicationClass, springContextConfigLocation, springContext)
    }

    @Override
    ServletHolder servletHolder(ServletContextHandler handler) {
        if (applicationClass) {
            new ServletHolder(new ServletContainer(applicationClass))
        } else if (springContextConfigLocation) {
            handler.addEventListener(new ContextLoaderListener())
            handler.setInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, springContextConfigLocation)
            new ServletHolder(new SpringServlet())
        } else {
            new ServletHolder(new ExistingContextSpringServlet(springContext))
        }
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
