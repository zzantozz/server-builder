package org.serverbuilder;

import com.sun.jersey.api.client.WebResource;

public interface ServerBuilder<ServerType, BuilderType extends ServerBuilder<ServerType, BuilderType>> {
    public BuilderType onPort(int port);

    public WebResource jerseyResource();

    public ServerType build();

    public void stop(ServerType server);
}
