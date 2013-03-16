package org.serverbuilder

import org.eclipse.jetty.server.Server

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
