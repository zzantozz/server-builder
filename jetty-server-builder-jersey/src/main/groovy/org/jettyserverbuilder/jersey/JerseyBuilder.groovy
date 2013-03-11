package org.jettyserverbuilder.jersey
import com.sun.jersey.spi.container.servlet.ServletContainer
import com.sun.jersey.spi.spring.container.servlet.SpringServlet
import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import org.eclipse.jetty.server.Handler
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.jettyserverbuilder.AbstractJettyServerBuilder
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
class JerseyBuilder extends AbstractJettyServerBuilder {
    final String contextPath = '/'
    final int port = 8080
    final String jerseyServletUrlPattern = '/*'
    final Class<? extends Application> applicationClass
    final String springContextConfigLocation
    final ConfigurableApplicationContext springContext

    static JerseyBuilder newJerseyServer(Class<? extends Application> applicationClass) {
        new JerseyBuilder().withApplicationClass(applicationClass)
    }

    static JerseyBuilder newJerseyServer(String springContextConfigLocation) {
        new JerseyBuilder().withSpringContextConfigLocation(springContextConfigLocation)
    }

    static JerseyBuilder newJerseyServer(ConfigurableApplicationContext springContext) {
        new JerseyBuilder().withSpringContext(springContext)
    }

    JerseyBuilder withApplicationClass(Class<? extends Application> applicationClass) {
        new JerseyBuilder(contextPath, port, jerseyServletUrlPattern, applicationClass, springContextConfigLocation, springContext)
    }

    JerseyBuilder withSpringContextConfigLocation(String springContextConfigLocation) {
        new JerseyBuilder(contextPath, port, jerseyServletUrlPattern, applicationClass, springContextConfigLocation, springContext)
    }

    JerseyBuilder withSpringContext(ConfigurableApplicationContext springContext) {
        new JerseyBuilder(contextPath, port, jerseyServletUrlPattern, applicationClass, springContextConfigLocation, springContext)
    }

    JerseyBuilder atContextPath(String contextPath) {
        if (!contextPath.startsWith('/')) {
            throw new IllegalArgumentException("Context path must start with a '/', was $contextPath")
        }
        new JerseyBuilder(contextPath, port, jerseyServletUrlPattern, applicationClass, springContextConfigLocation, springContext)
    }

    JerseyBuilder atRootContextPath() {
        def contextPath = '/'
        new JerseyBuilder(contextPath, port, jerseyServletUrlPattern, applicationClass, springContextConfigLocation, springContext)
    }

    JerseyBuilder onPort(int port) {
        new JerseyBuilder(contextPath, port, jerseyServletUrlPattern, applicationClass, springContextConfigLocation, springContext)
    }

    JerseyBuilder mappedTo(String jerseyServletUrlPattern) {
        if (!jerseyServletUrlPattern.startsWith('/')) {
            throw new IllegalArgumentException("A servlet mapping must start with a '/', was $jerseyServletUrlPattern")
        }
        new JerseyBuilder(contextPath, port, jerseyServletUrlPattern, applicationClass, springContextConfigLocation, springContext)
    }

    Handler handler() {
        def handler = new ServletContextHandler()
        ServletHolder servletHolder
        if (applicationClass) {
            servletHolder = new ServletHolder(new ServletContainer(applicationClass))
        } else if (springContextConfigLocation) {
            handler.addEventListener(new ContextLoaderListener())
            handler.setInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, springContextConfigLocation)
            servletHolder = new ServletHolder(new SpringServlet())
        } else {
            servletHolder = new ServletHolder(new ExistingContextSpringServlet(springContext))
        }
        handler.addServlet(servletHolder, jerseyServletUrlPattern)
        handler.contextPath = contextPath
        handler
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
