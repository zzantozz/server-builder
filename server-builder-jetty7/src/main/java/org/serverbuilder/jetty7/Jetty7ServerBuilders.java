package org.serverbuilder.jetty7;

import org.eclipse.jetty.server.Server;
import org.serverbuilder.FileServerBuilder;
import org.serverbuilder.JerseyServerBuilder;
import org.serverbuilder.ServerBuilders;
import org.serverbuilder.SpringMvcServerBuilder;

public class Jetty7ServerBuilders implements ServerBuilders<Server> {
    @Override
    public FileServerBuilder<Server> newFileServer() {
        return new Jetty7FileServerBuilder();
    }

    @Override
    public JerseyServerBuilder<Server> newJerseyServer() {
        return new Jetty7JerseyServerBuilder();
    }

    @Override
    public SpringMvcServerBuilder<Server> newSpringMvcServer() {
        return new Jetty7SpringMvcBuilder();
    }

}
