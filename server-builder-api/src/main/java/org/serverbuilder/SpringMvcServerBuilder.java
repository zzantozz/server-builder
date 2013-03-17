package org.serverbuilder;

import org.springframework.web.context.WebApplicationContext;

public interface SpringMvcServerBuilder<ServerType> extends ServletBasedServerBuilder<ServerType, SpringMvcServerBuilder<ServerType>> {
    SpringMvcServerBuilder<ServerType> withRootContext(WebApplicationContext rootContext);
    SpringMvcServerBuilder<ServerType> withDispatcherContext(WebApplicationContext dispatcherContext);
}
