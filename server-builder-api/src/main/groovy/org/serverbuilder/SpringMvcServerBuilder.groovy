package org.serverbuilder

import groovy.transform.CompileStatic
import org.springframework.web.context.WebApplicationContext

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/14/13
 * Time: 11:01 PM
 */
@CompileStatic
interface SpringMvcServerBuilder<ServerType> extends
        ServletBasedServerBuilder<ServerType, SpringMvcServerBuilder<ServerType>> {
    SpringMvcServerBuilder<ServerType> withRootContext(WebApplicationContext rootContext)
    SpringMvcServerBuilder<ServerType> withDispatcherContext(WebApplicationContext dispatcherContext)
}
