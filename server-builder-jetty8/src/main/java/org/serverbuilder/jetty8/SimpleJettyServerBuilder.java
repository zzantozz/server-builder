package org.serverbuilder.jetty8;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.serverbuilder.ServerBuilder;

public abstract class SimpleJettyServerBuilder<BuilderType extends SimpleJettyServerBuilder<BuilderType>> implements ServerBuilder<Server, BuilderType> {
    public SimpleJettyServerBuilder(int port) {
        this.port = port;
    }

    public SimpleJettyServerBuilder() {
        this(8080);
    }

    @Override
    public WebResource jerseyResource() {
        return Client.create().resource("http://localhost:" + String.valueOf(getPort()));
    }

    @Override
    public Server build() {
        final Server server = new Server(getPort());
        server.setHandler(handler(server));
        try {
            server.start();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to start server", e);
        }
        return server;
    }

    @Override
    public void stop(Server server) {
        try {
            server.stop();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to stop server", e);
        }
    }

    public Handler handler(Server server) {
        return new DefaultHandler();
    }

    public int getPort() {
        return port;
    }

    private final int port;
}
