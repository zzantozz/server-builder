package org.jettyserverbuilder.jersey
import com.sun.jersey.spi.container.servlet.ServletContainer
import com.sun.jersey.spi.spring.container.servlet.SpringServlet
import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import org.eclipse.jetty.server.Server
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
        new JerseyBuilder(this.contextPath, this.port, this.jerseyServletUrlPattern, applicationClass, this.springContextConfigLocation, this.springContext)
    }

    JerseyBuilder withSpringContextConfigLocation(String springContextConfigLocation) {
        new JerseyBuilder(this.contextPath, this.port, this.jerseyServletUrlPattern, this.applicationClass, springContextConfigLocation, this.springContext)
    }

    JerseyBuilder withSpringContext(ConfigurableApplicationContext springContext) {
        new JerseyBuilder(this.contextPath, this.port, this.jerseyServletUrlPattern, this.applicationClass, this.springContextConfigLocation, springContext)
    }

    JerseyBuilder atContextPath(String contextPath) {
        if (!contextPath.startsWith('/')) {
            throw new IllegalArgumentException("Context path must start with a '/', was $contextPath")
        }
        new JerseyBuilder(contextPath, this.port, this.jerseyServletUrlPattern, this.applicationClass, this.springContextConfigLocation, this.springContext)
    }

    JerseyBuilder atRootContextPath() {
        new JerseyBuilder('/', this.port, this.jerseyServletUrlPattern, this.applicationClass, this.springContextConfigLocation, this.springContext)
    }

    JerseyBuilder onPort(int port) {
        new JerseyBuilder(this.contextPath, port, this.jerseyServletUrlPattern, this.applicationClass, this.springContextConfigLocation, this.springContext)
    }

    JerseyBuilder mappedTo(String jerseyServletUrlPattern) {
        if (!jerseyServletUrlPattern.startsWith('/')) {
            throw new IllegalArgumentException("A servlet mapping must start with a '/', was $jerseyServletUrlPattern")
        }
        new JerseyBuilder(this.contextPath, this.port, jerseyServletUrlPattern, this.applicationClass, this.springContextConfigLocation, this.springContext)
    }

    @Override
    Server server() {
        def server = new Server(port)
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
        server.handler = handler
        server.start()
        server
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
