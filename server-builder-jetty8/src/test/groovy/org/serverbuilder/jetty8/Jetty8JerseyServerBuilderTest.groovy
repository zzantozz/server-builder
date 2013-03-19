package org.serverbuilder.jetty8

import org.eclipse.jetty.server.Server
import org.serverbuilder.JerseyServerBuilderTest
import org.serverbuilder.ServersOld

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/16/13
 * Time: 8:25 AM
 */
class Jetty8JerseyServerBuilderTest extends JerseyServerBuilderTest<Server> {
    Jetty8JerseyServerBuilderTest() {
        super(ServersOld.jetty8())
    }
}
