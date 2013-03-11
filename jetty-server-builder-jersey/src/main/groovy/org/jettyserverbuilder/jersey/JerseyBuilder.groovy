package org.jettyserverbuilder.jersey
import com.sun.jersey.spi.container.servlet.ServletContainer
import com.sun.jersey.spi.spring.container.servlet.SpringServlet
import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.jettyserverbuilder.SimpleServletBasedJettyServerBuilder
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
@TupleConstructor
class JerseyBuilder extends SimpleServletBasedJettyServerBuilder {
    final Class<? extends Application> applicationClass
    final String springContextConfigLocation
    final ConfigurableApplicationContext springContext

    JerseyBuilder(Class<? extends Application> applicationClass) {
        this.applicationClass = applicationClass
    }

    JerseyBuilder(String springContextConfigLocation) {
        this.springContextConfigLocation = springContextConfigLocation
    }

    JerseyBuilder(ConfigurableApplicationContext springContext) {
        this.springContext = springContext
    }

    JerseyBuilder(String contextPath, int port, String urlPattern, Class<? extends Application> applicationClass, String springContextConfigLocation, ConfigurableApplicationContext springContext) {
        super(port, contextPath, urlPattern)
        this.applicationClass = applicationClass
        this.springContextConfigLocation = springContextConfigLocation
        this.springContext = springContext
    }

    static JerseyBuilder newJerseyServer(Class<? extends Application> applicationClass) {
        new JerseyBuilder(applicationClass)
    }

    static JerseyBuilder newJerseyServer(String springContextConfigLocation) {
        new JerseyBuilder(springContextConfigLocation)
    }

    static JerseyBuilder newJerseyServer(ConfigurableApplicationContext springContext) {
        new JerseyBuilder(springContext)
    }

    JerseyBuilder withApplicationClass(Class<? extends Application> applicationClass) {
        new JerseyBuilder(contextPath, port, urlPattern, applicationClass, springContextConfigLocation, springContext)
    }

    JerseyBuilder withSpringContextConfigLocation(String springContextConfigLocation) {
        new JerseyBuilder(contextPath, port, urlPattern, applicationClass, springContextConfigLocation, springContext)
    }

    JerseyBuilder withSpringContext(ConfigurableApplicationContext springContext) {
        new JerseyBuilder(contextPath, port, urlPattern, applicationClass, springContextConfigLocation, springContext)
    }

    JerseyBuilder atContextPath(String contextPath) {
        if (!contextPath.startsWith('/')) {
            throw new IllegalArgumentException("Context path must start with a '/', was $contextPath")
        }
        new JerseyBuilder(contextPath, port, urlPattern, applicationClass, springContextConfigLocation, springContext)
    }

    JerseyBuilder atRootContextPath() {
        def contextPath = '/'
        new JerseyBuilder(contextPath, port, urlPattern, applicationClass, springContextConfigLocation, springContext)
    }

    JerseyBuilder onPort(int port) {
        new JerseyBuilder(contextPath, port, urlPattern, applicationClass, springContextConfigLocation, springContext)
    }

    JerseyBuilder mappedTo(String urlPattern) {
        if (!urlPattern.startsWith('/')) {
            throw new IllegalArgumentException("A servlet mapping must start with a '/', was $urlPattern")
        }
        new JerseyBuilder(contextPath, port, urlPattern, applicationClass, springContextConfigLocation, springContext)
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
