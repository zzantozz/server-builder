package org.serverbuilder.jetty8

import org.eclipse.jetty.server.Server
import org.serverbuilder.FileServerBuilderTest
import org.serverbuilder.ServersOld

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/16/13
 * Time: 8:24 AM
 */
class Jetty8FileServerBuilderTest extends FileServerBuilderTest<Server> {
    Jetty8FileServerBuilderTest() {
        super(ServersOld.jetty8())
    }
}
