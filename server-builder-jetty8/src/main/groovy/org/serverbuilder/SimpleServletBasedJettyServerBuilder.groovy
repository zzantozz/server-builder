package org.serverbuilder

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
class SimpleServletBasedJettyServerBuilder extends SimpleJettyServerBuilder
        implements ServletBasedServerBuilder {
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
