package org.serverbuilder;

import org.serverbuilder.impl.Binding;

public class Servers {
    public static <T> FileServerBuilder<T> newFileServer(T serverType) {
        return Binding.findServerBuilders(serverType).newFileServer();
    }

    public static <T> JerseyServerBuilder<T> newJerseyServer(T serverType) {
        return Binding.findServerBuilders(serverType).newJerseyServer();
    }

    public static <T> SpringMvcServerBuilder<T> newSpringMvcServer(T serverType) {
        return Binding.findServerBuilders(serverType).newSpringMvcServer();
    }
}
