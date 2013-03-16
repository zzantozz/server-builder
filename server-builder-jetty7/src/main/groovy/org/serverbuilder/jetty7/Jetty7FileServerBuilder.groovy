package org.serverbuilder.jetty7

import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.WebResource
import groovy.transform.CompileStatic
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.ResourceHandler
import org.serverbuilder.FileServerBuilder
/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/10/13
 * Time: 6:11 PM
 */
@CompileStatic
class Jetty7FileServerBuilder implements FileServerBuilder<Server> {
    String resourceBase = '.'
    String[] welcomeFiles = []
    int port = 8080
    boolean directoriesListed = true

    @Override
    FileServerBuilder<Server> atResourceBase(String resourceBase) {
        this.resourceBase = resourceBase
        this
    }

    @Override
    FileServerBuilder<Server> withoutDirectoriesListed() {
        this.directoriesListed = false
        this
    }

    @Override
    FileServerBuilder<Server> withWelcomeFiles(String... welcomeFiles) {
        this.welcomeFiles = welcomeFiles
        this
    }

    @Override
    FileServerBuilder<Server> onPort(int port) {
        this.port = port
        this
    }

    @Override
    WebResource jerseyResource() {
        Client.create().resource("http://localhost:$port")
    }

    @Override
    Server build() {
        def server = new Server(port)
        def handler = new ResourceHandler()
        handler.with {
            it.directoriesListed = this.directoriesListed
            it.welcomeFiles = this.welcomeFiles
            it.resourceBase = this.resourceBase
        }
        server.handler = handler
        server.start()
        server
    }

    @Override
    void stop(Server server) {
        server.stop()
    }
}
