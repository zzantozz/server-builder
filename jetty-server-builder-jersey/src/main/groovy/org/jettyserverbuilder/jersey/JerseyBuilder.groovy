package org.jettyserverbuilder.jersey
import com.sun.jersey.spi.container.servlet.ServletContainer
import com.sun.jersey.spi.spring.container.servlet.SpringServlet
import groovy.transform.CompileStatic
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.jettyserverbuilder.AbstractJettyServerBuilder
import org.jettyserverbuilder.JettyServerBuilder
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
class JerseyBuilder extends AbstractJettyServerBuilder {
    final String contextPath = '/'
    final int port = 8080
    Class<? extends Application> applicationClass
    String springContextConfigLocation
    ConfigurableApplicationContext springContext

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
        this.applicationClass = applicationClass
        this
    }

    JerseyBuilder withSpringContextConfigLocation(String springContextConfigLocation) {
        this.springContextConfigLocation = springContextConfigLocation
        this
    }

    JerseyBuilder withSpringContext(ConfigurableApplicationContext applicationContext) {
        this.springContext = applicationContext
        this
    }

    @Override
    JettyServerBuilder atContextPath(String contextPath) {
        return null  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    JettyServerBuilder atRootContextPath() {
        return null  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    JettyServerBuilder onPort(int port) {
        return null  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    Server server() {
        def server = new Server(port)
        def handler = new ServletContextHandler()
        if (applicationClass) {
            handler.addServlet(new ServletHolder(new ServletContainer(applicationClass)), '/*')
        } else if (springContextConfigLocation) {
            handler.addEventListener(new ContextLoaderListener())
            handler.setInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, springContextConfigLocation)
            handler.addServlet(new ServletHolder(new SpringServlet()), '/*')
        } else {
            handler.addServlet(new ServletHolder(new ExistingContextSpringServlet(springContext)), '/*')
        }
        server.setHandler(handler)
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
