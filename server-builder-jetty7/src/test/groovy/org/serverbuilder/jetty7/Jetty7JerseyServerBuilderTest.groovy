package org.serverbuilder.jetty7

import org.eclipse.jetty.server.Server
import org.serverbuilder.JerseyServerBuilderTest
import org.serverbuilder.Servers

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/16/13
 * Time: 8:25 AM
 */
class Jetty7JerseyServerBuilderTest extends JerseyServerBuilderTest<Server> {
    Jetty7JerseyServerBuilderTest() {
        super(Servers.jetty7())
    }
}
