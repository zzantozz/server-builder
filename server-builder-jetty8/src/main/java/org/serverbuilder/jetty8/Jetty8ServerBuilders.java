package org.serverbuilder.jetty8;

import org.eclipse.jetty.server.Server;
import org.serverbuilder.FileServerBuilder;
import org.serverbuilder.JerseyServerBuilder;
import org.serverbuilder.ServerBuilders;
import org.serverbuilder.SpringMvcServerBuilder;

public class Jetty8ServerBuilders implements ServerBuilders<Server> {
    @Override
    public FileServerBuilder<Server> newFileServer() {
        return new Jetty8FileServerBuilder();
    }

    @Override
    public JerseyServerBuilder<Server> newJerseyServer() {
        return new Jetty8JerseyServerBuilder();
    }

    @Override
    public SpringMvcServerBuilder<Server> newSpringMvcServer() {
        return new Jetty8SpringMvcBuilder();
    }

}
