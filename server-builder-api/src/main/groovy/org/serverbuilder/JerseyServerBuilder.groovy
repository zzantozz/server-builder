package org.serverbuilder

import groovy.transform.CompileStatic
import org.springframework.context.ConfigurableApplicationContext

import javax.ws.rs.core.Application

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/14/13
 * Time: 11:04 PM
 */
@CompileStatic
interface JerseyServerBuilder<ServerType> extends
        ServletBasedServerBuilder<ServerType, JerseyServerBuilder<ServerType>> {
    JerseyServerBuilder<ServerType> withApplicationClass(Class<? extends Application> applicationClass)
    JerseyServerBuilder<ServerType> withSpringContextConfigLocation(String springContextConfigLocation)
    JerseyServerBuilder<ServerType> withSpringContext(ConfigurableApplicationContext springContext)
}