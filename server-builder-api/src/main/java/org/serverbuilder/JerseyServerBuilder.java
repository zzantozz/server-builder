package org.serverbuilder;

import org.springframework.context.ConfigurableApplicationContext;

import javax.ws.rs.core.Application;

public interface JerseyServerBuilder<ServerType> extends ServletBasedServerBuilder<ServerType, JerseyServerBuilder<ServerType>> {
    JerseyServerBuilder<ServerType> withApplicationClass(Class<? extends Application> applicationClass);
    JerseyServerBuilder<ServerType> withSpringContextConfigLocation(String springContextConfigLocation);
    JerseyServerBuilder<ServerType> withSpringContext(ConfigurableApplicationContext springContext);
}
