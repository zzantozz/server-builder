package org.serverbuilder.jetty8

import org.eclipse.jetty.server.Server
import org.serverbuilder.Servers
import org.serverbuilder.SpringMvcServerBuilderTest

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/16/13
 * Time: 8:25 AM
 */
class Jetty8SpringMvcBuilderTest extends SpringMvcServerBuilderTest<Server> {
    Jetty8SpringMvcBuilderTest() {
        super(Servers.jetty8())
    }
}
