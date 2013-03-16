package org.jettyserverbuilder

import com.sun.jersey.api.client.WebResource
import groovy.transform.CompileStatic

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/9/13
 * Time: 11:06 AM
 */
@CompileStatic
public interface ServerBuilder<ServerType, BuilderType extends
        ServerBuilder<ServerType, BuilderType>> {
    BuilderType onPort(int port)
    WebResource jerseyResource()
    ServerType build()
    void stop(ServerType server)
}
