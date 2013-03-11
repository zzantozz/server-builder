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
abstract class AbstractServletBasedJettyServerBuilder extends AbstractJettyServerBuilder
        implements ServletBasedJettyServerBuilder {
    @Override
    Handler handler(Server server) {
        def handler = new ServletContextHandler()
        handler.contextPath = contextPath
        handler.addServlet(servletHolder(handler), getUrlPattern())
        handler
    }

    abstract ServletHolder servletHolder(ServletContextHandler handler)
}
