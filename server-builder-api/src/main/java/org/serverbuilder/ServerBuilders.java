package org.serverbuilder;

public interface ServerBuilders<ServerType> {
    FileServerBuilder<ServerType> newFileServer();
    JerseyServerBuilder<ServerType> newJerseyServer();
    SpringMvcServerBuilder<ServerType> newSpringMvcServer();
}
