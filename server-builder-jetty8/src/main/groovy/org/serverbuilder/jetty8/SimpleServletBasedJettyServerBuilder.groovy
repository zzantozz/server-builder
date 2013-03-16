package org.serverbuilder.jetty8

import groovy.transform.CompileStatic
import org.eclipse.jetty.server.Handler
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.serverbuilder.ServletBasedServerBuilder

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/10/13
 * Time: 7:41 PM
 */
@CompileStatic
abstract class SimpleServletBasedJettyServerBuilder<BuilderType extends SimpleServletBasedJettyServerBuilder<BuilderType>>
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
