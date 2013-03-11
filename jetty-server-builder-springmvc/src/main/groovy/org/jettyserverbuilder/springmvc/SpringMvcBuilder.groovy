package org.jettyserverbuilder.springmvc
import groovy.transform.CompileStatic
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.jettyserverbuilder.SimpleServletBasedJettyServerBuilder
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
class SpringMvcBuilder extends SimpleServletBasedJettyServerBuilder {
    final WebApplicationContext rootContext
    final WebApplicationContext dispatcherContext

    SpringMvcBuilder(WebApplicationContext dispatcherContext) {
        this.dispatcherContext = dispatcherContext
    }

    SpringMvcBuilder(int port, String contextPath, String urlPattern, WebApplicationContext rootContext, WebApplicationContext dispatcherContext) {
        super(port, contextPath, urlPattern)
        this.rootContext = rootContext
        this.dispatcherContext = dispatcherContext
    }

    static SpringMvcBuilder newSpringMvcServer(WebApplicationContext dispatcherContext) {
        new SpringMvcBuilder(dispatcherContext)
    }

    SpringMvcBuilder withRootContext(WebApplicationContext rootContext) {
        new SpringMvcBuilder(port, contextPath, urlPattern, rootContext, dispatcherContext)
    }

    SpringMvcBuilder atContextPath(String contextPath) {
        if (!contextPath.startsWith('/')) {
            throw new IllegalArgumentException("Context path must start with a '/', was $contextPath")
        }
        new SpringMvcBuilder(port, contextPath, urlPattern, rootContext, dispatcherContext)
    }

    SpringMvcBuilder atRootContextPath() {
        def contextPath = '/'
        new SpringMvcBuilder(port, contextPath, urlPattern, rootContext, dispatcherContext)
    }

    SpringMvcBuilder mappedTo(String urlPattern) {
        if (!urlPattern.startsWith('/')) {
            throw new IllegalArgumentException("A servlet mapping must start with a '/', was $urlPattern")
        }
        new SpringMvcBuilder(port, contextPath, urlPattern, rootContext, dispatcherContext)
    }

    SpringMvcBuilder onPort(int port) {
        new SpringMvcBuilder(port, contextPath, urlPattern, rootContext, dispatcherContext)
    }

    @Override
    ServletHolder servletHolder(ServletContextHandler handler) {
        if (rootContext) handler.addEventListener(new ContextLoaderListener(rootContext))
        new ServletHolder(new DispatcherServlet(dispatcherContext))
    }
}
