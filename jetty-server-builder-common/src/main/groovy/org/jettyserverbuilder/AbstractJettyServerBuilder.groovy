package org.jettyserverbuilder

import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.WebResource
import groovy.transform.CompileStatic
import org.eclipse.jetty.server.Handler
import org.eclipse.jetty.server.Server

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/9/13
 * Time: 2:17 PM
 */
@CompileStatic
abstract class AbstractJettyServerBuilder implements JettyServerBuilder {
    WebResource jerseyResource() {
        Client.create().resource("http://localhost:${getPort()}")
    }

    @Override
    Server server() {
        Server server = new Server(getPort())
        server.with {
            handler = handler()
            start()
        }
        server
    }

    abstract Handler handler()
}
