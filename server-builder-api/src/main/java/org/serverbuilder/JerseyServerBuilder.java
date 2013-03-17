package org.serverbuilder;

import org.springframework.context.ConfigurableApplicationContext;

import javax.ws.rs.core.Application;

public interface JerseyServerBuilder<ServerType> extends ServletBasedServerBuilder<ServerType, JerseyServerBuilder<ServerType>> {
    public JerseyServerBuilder<ServerType> withApplicationClass(Class<? extends Application> applicationClass);

    public JerseyServerBuilder<ServerType> withSpringContextConfigLocation(String springContextConfigLocation);

    public JerseyServerBuilder<ServerType> withSpringContext(ConfigurableApplicationContext springContext);
}
