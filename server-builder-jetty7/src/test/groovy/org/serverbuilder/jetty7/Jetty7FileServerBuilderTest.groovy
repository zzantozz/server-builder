package org.serverbuilder.jetty7

import org.eclipse.jetty.server.Server
import org.serverbuilder.FileServerBuilderTest
import org.serverbuilder.ServersOld

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/16/13
 * Time: 8:24 AM
 */
class Jetty7FileServerBuilderTest extends FileServerBuilderTest<Server> {
    Jetty7FileServerBuilderTest() {
        super(ServersOld.jetty7())
    }
}
