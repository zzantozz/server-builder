package org.serverbuilder

import org.springframework.web.context.WebApplicationContext

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/14/13
 * Time: 11:01 PM
 */
public interface SpringMvcServerBuilder<ServerType> extends
        ServletBasedServerBuilder<ServerType, SpringMvcServerBuilder<ServerType>> {
    SpringMvcServerBuilder<ServerType> withRootContext(WebApplicationContext rootContext)
    SpringMvcServerBuilder<ServerType> withDispatcherContext(WebApplicationContext dispatcherContext)
}
