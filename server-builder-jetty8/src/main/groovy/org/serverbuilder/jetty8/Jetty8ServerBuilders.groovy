package org.serverbuilder.jetty8

import org.eclipse.jetty.server.Server
import org.serverbuilder.FileServerBuilder
import org.serverbuilder.JerseyServerBuilder
import org.serverbuilder.ServerBuilders
import org.serverbuilder.SpringMvcServerBuilder

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/16/13
 * Time: 9:07 AM
 */
class Jetty8ServerBuilders implements ServerBuilders<Server> {
    @Override
    FileServerBuilder newFileServer() {
        new Jetty8FileServerBuilder()
    }

    @Override
    JerseyServerBuilder newJerseyServer() {
        new Jetty8JerseyServerBuilder()
    }

    @Override
    SpringMvcServerBuilder newSpringMvcServer() {
        new Jetty8SpringMvcBuilder()
    }
}
