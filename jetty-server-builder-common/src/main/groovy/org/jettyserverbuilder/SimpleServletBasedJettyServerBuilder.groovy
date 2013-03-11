package org.jettyserverbuilder
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
        implements ServletBasedJettyServerBuilder {
    String contextPath = '/'
    String urlPattern = '/*'

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
