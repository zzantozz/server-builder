package org.jettyserverbuilder

import com.sun.jersey.api.client.WebResource
import groovy.transform.CompileStatic
import org.eclipse.jetty.server.Server

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/9/13
 * Time: 11:06 AM
 */
@CompileStatic
public interface JettyServerBuilder {
    int getPort()
    WebResource jerseyResource()
    Server server()
}
