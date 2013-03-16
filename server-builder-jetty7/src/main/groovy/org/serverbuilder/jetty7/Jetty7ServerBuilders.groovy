package org.serverbuilder.jetty7

import org.eclipse.jetty.server.Server
import org.serverbuilder.FileServerBuilder
import org.serverbuilder.JerseyServerBuilder
import org.serverbuilder.ServerBuilders
import org.serverbuilder.SpringMvcServerBuilder

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/16/13
 * Time: 11:25 AM
 */
class Jetty7ServerBuilders implements ServerBuilders<Server> {
    @Override
    FileServerBuilder<Server> newFileServer() {
        new Jetty7FileServerBuilder()
    }

    @Override
    JerseyServerBuilder<Server> newJerseyServer() {
        new Jetty7JerseyServerBuilder()
    }

    @Override
    SpringMvcServerBuilder<Server> newSpringMvcServer() {
        new Jetty7SpringMvcBuilder()
    }
}
