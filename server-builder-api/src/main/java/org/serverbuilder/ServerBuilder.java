package org.serverbuilder;

import com.sun.jersey.api.client.WebResource;

public interface ServerBuilder<ServerType, BuilderType extends ServerBuilder<ServerType, BuilderType>> {
    BuilderType onPort(int port);
    WebResource jerseyResource();
    ServerType build();
    void stop(ServerType server);
}
