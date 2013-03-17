package org.serverbuilder;

public interface ServerBuilders<ServerType> {
    public FileServerBuilder<ServerType> newFileServer();

    public JerseyServerBuilder<ServerType> newJerseyServer();

    public SpringMvcServerBuilder<ServerType> newSpringMvcServer();
}
