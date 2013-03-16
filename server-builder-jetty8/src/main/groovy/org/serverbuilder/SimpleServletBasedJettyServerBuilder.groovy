package org.serverbuilder

import groovy.transform.CompileStatic
import org.eclipse.jetty.server.Handler
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/10/13
 * Time: 7:41 PM
 */
@CompileStatic
class SimpleServletBasedJettyServerBuilder<BuilderType extends SimpleServletBasedJettyServerBuilder<BuilderType>>
        extends SimpleJettyServerBuilder<BuilderType>
        implements ServletBasedServerBuilder<Server, BuilderType> {
    final String contextPath
    final String urlPattern

    SimpleServletBasedJettyServerBuilder(String contextPath = '/', String urlPattern = '/*') {
        this.contextPath = contextPath
        this.urlPattern = urlPattern
    }

    SimpleServletBasedJettyServerBuilder(int port, String contextPath = '/', String urlPattern = '/*') {
        super(port)
        this.contextPath = contextPath
        this.urlPattern = urlPattern
    }

    @Override
    BuilderType atContextPath(String contextPath) {
        // TODO: Write me!
        throw new UnsupportedOperationException("Write me! - org.serverbuilder.ServletBasedServerBuilder.atContextPath")
    }

    @Override
    BuilderType atRootContextPath() {
        // TODO: Write me!
        throw new UnsupportedOperationException("Write me! - org.serverbuilder.ServletBasedServerBuilder.atRootContextPath")
    }

    @Override
    BuilderType mappedTo(String urlPattern) {
        // TODO: Write me!
        throw new UnsupportedOperationException("Write me! - org.serverbuilder.ServletBasedServerBuilder.mappedTo")
    }

    @Override
    Handler handler(Server server) {
        def handler = new ServletContextHandler()
        handler.contextPath = getContextPath()
        handler.addServlet(servletHolder(handler), getUrlPattern())
        handler
    }

    ServletHolder servletHolder(ServletContextHandler handler) {
        new ServletHolder()
    }
}
