package org.jettyserverbuilder

import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.WebResource
import groovy.transform.CompileStatic

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/9/13
 * Time: 2:17 PM
 */
@CompileStatic
abstract class AbstractJettyServerBuilder implements JettyServerBuilder {
    WebResource jerseyResource() {
        Client.create().resource("http://localhost:${getPort()}")
    }
}
