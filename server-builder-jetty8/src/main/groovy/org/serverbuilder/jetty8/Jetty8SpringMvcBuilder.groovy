package org.serverbuilder.jetty8
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
class Jetty8SpringMvcBuilder extends SimpleServletBasedJettyServerBuilder<Jetty8SpringMvcBuilder> implements SpringMvcServerBuilder<Server> {
    final WebApplicationContext rootContext
    final WebApplicationContext dispatcherContext

    Jetty8SpringMvcBuilder(WebApplicationContext dispatcherContext) {
        this.dispatcherContext = dispatcherContext
    }

    Jetty8SpringMvcBuilder(int port, String contextPath, String urlPattern, WebApplicationContext rootContext, WebApplicationContext dispatcherContext) {
        super(port, contextPath, urlPattern)
        this.rootContext = rootContext
        this.dispatcherContext = dispatcherContext
    }

    static Jetty8SpringMvcBuilder newSpringMvcServer(WebApplicationContext dispatcherContext) {
        new Jetty8SpringMvcBuilder(dispatcherContext)
    }

    @Override
    SpringMvcServerBuilder<Server> withDispatcherContext(WebApplicationContext dispatcherContext) {
        // TODO: Write me!
        throw new UnsupportedOperationException("Write me! - org.serverbuilder.SpringMvcServerBuilder.withDispatcherContext")
    }

    Jetty8SpringMvcBuilder withRootContext(WebApplicationContext rootContext) {
        new Jetty8SpringMvcBuilder(port, contextPath, urlPattern, rootContext, dispatcherContext)
    }

    Jetty8SpringMvcBuilder atContextPath(String contextPath) {
        if (!contextPath.startsWith('/')) {
            throw new IllegalArgumentException("Context path must start with a '/', was $contextPath")
        }
        new Jetty8SpringMvcBuilder(port, contextPath, urlPattern, rootContext, dispatcherContext)
    }

    Jetty8SpringMvcBuilder atRootContextPath() {
        def contextPath = '/'
        new Jetty8SpringMvcBuilder(port, contextPath, urlPattern, rootContext, dispatcherContext)
    }

    Jetty8SpringMvcBuilder mappedTo(String urlPattern) {
        if (!urlPattern.startsWith('/')) {
            throw new IllegalArgumentException("A servlet mapping must start with a '/', was $urlPattern")
        }
        new Jetty8SpringMvcBuilder(port, contextPath, urlPattern, rootContext, dispatcherContext)
    }

    Jetty8SpringMvcBuilder onPort(int port) {
        new Jetty8SpringMvcBuilder(port, contextPath, urlPattern, rootContext, dispatcherContext)
    }

    @Override
    ServletHolder servletHolder(ServletContextHandler handler) {
        if (rootContext) handler.addEventListener(new ContextLoaderListener(rootContext))
        new ServletHolder(new DispatcherServlet(dispatcherContext))
    }
}
