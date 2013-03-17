package org.serverbuilder;

import org.springframework.web.context.WebApplicationContext;

public interface SpringMvcServerBuilder<ServerType> extends ServletBasedServerBuilder<ServerType, SpringMvcServerBuilder<ServerType>> {
    public SpringMvcServerBuilder<ServerType> withRootContext(WebApplicationContext rootContext);

    public SpringMvcServerBuilder<ServerType> withDispatcherContext(WebApplicationContext dispatcherContext);
}
