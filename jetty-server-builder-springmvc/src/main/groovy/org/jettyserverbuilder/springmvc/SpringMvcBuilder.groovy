package org.jettyserverbuilder.springmvc
import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.jettyserverbuilder.AbstractServletBasedJettyServerBuilder
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
@TupleConstructor
class SpringMvcBuilder extends AbstractServletBasedJettyServerBuilder {
    final String contextPath = '/'
    final int port = 8080
    final WebApplicationContext rootContext
    final WebApplicationContext dispatcherContext
    final String dispatcherServletUrlPattern = '/*'

    static SpringMvcBuilder newSpringMvcServer(WebApplicationContext dispatcherContext) {
        new SpringMvcBuilder().withDispatcherContext(dispatcherContext)
    }

    SpringMvcBuilder withRootContext(WebApplicationContext rootContext) {
        new SpringMvcBuilder(contextPath, port, rootContext, dispatcherContext, dispatcherServletUrlPattern)
    }

    SpringMvcBuilder withDispatcherContext(WebApplicationContext dispatcherContext) {
        new SpringMvcBuilder(contextPath, port, rootContext, dispatcherContext, dispatcherServletUrlPattern)
    }

    SpringMvcBuilder atContextPath(String contextPath) {
        if (!contextPath.startsWith('/')) {
            throw new IllegalArgumentException("Context path must start with a '/', was $contextPath")
        }
        new SpringMvcBuilder(contextPath, port, rootContext, dispatcherContext, dispatcherServletUrlPattern)
    }

    SpringMvcBuilder atRootContextPath() {
        def contextPath = '/'
        new SpringMvcBuilder(contextPath, port, rootContext, dispatcherContext, dispatcherServletUrlPattern)
    }

    SpringMvcBuilder mappedTo(String dispatcherServletMapping) {
        if (!dispatcherServletMapping.startsWith('/')) {
            throw new IllegalArgumentException("A servlet mapping must start with a '/', was $dispatcherServletMapping")
        }
        new SpringMvcBuilder(contextPath, port, rootContext, dispatcherContext, dispatcherServletMapping)
    }

    SpringMvcBuilder onPort(int port) {
        new SpringMvcBuilder(contextPath, port, rootContext, dispatcherContext, dispatcherServletUrlPattern)
    }

    @Override
    String getUrlPattern() {
        dispatcherServletUrlPattern
    }

    @Override
    ServletHolder servletHolder(ServletContextHandler handler) {
        if (rootContext) handler.addEventListener(new ContextLoaderListener(rootContext))
        new ServletHolder(new DispatcherServlet(dispatcherContext))
    }
}
