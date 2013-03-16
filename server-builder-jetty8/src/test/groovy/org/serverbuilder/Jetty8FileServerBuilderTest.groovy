package org.serverbuilder

import org.eclipse.jetty.server.Server

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/16/13
 * Time: 8:24 AM
 */
class Jetty8FileServerBuilderTest extends FileServerBuilderTest<Server> {
    Jetty8FileServerBuilderTest(ServerBuilders<Server> builders) {
        super(Servers.jetty8())
    }
}
