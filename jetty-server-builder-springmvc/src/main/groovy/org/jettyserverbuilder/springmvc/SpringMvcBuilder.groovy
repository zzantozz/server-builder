package org.jettyserverbuilder.springmvc

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.jettyserverbuilder.AbstractJettyServerBuilder
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
class SpringMvcBuilder extends AbstractJettyServerBuilder {
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
    Server server() {
        def server = new Server(port)
        def handler = new ServletContextHandler()
        if (rootContext) handler.addEventListener(new ContextLoaderListener(rootContext))
        def servletHolder = new ServletHolder(new DispatcherServlet(dispatcherContext))
        handler.addServlet(servletHolder, dispatcherServletUrlPattern)
        handler.contextPath = contextPath
        server.handler = handler
        server.start()
        server
    }
}
