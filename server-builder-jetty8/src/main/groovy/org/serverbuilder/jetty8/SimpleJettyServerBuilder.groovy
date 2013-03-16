package org.serverbuilder.jetty8

import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.WebResource
import groovy.transform.CompileStatic
import org.eclipse.jetty.server.Handler
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.DefaultHandler
import org.serverbuilder.ServerBuilder

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/9/13
 * Time: 2:17 PM
 */
@CompileStatic
class SimpleJettyServerBuilder<BuilderType extends SimpleJettyServerBuilder<BuilderType>>
        implements ServerBuilder<Server, BuilderType> {

    final int port

    SimpleJettyServerBuilder(int port = 8080) {
        this.port = port
    }

    @Override
    BuilderType onPort(int port) {
        // TODO: Write me!
        throw new UnsupportedOperationException("Write me! - org.serverbuilder.ServerBuilder.onPort")
    }

    @Override
    WebResource jerseyResource() {
        Client.create().resource("http://localhost:${getPort()}")
    }

    @Override
    Server build() {
        Server server = new Server(getPort())
        server.with {
            handler = handler(server)
            start()
        }
        server
    }

    @Override
    void stop(Server server) {
        // TODO: Write me!
        throw new UnsupportedOperationException("Write me! - org.serverbuilder.ServerBuilder.stop")
    }

    Handler handler(Server server) {
        new DefaultHandler()
    }
}
