package org.serverbuilder.jetty7

import org.eclipse.jetty.server.Server
import org.serverbuilder.ServersOld
import org.serverbuilder.SpringMvcServerBuilderTest

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/16/13
 * Time: 8:25 AM
 */
class Jetty7SpringMvcBuilderTest extends SpringMvcServerBuilderTest<Server> {
    Jetty7SpringMvcBuilderTest() {
        super(ServersOld.jetty7())
    }
}
