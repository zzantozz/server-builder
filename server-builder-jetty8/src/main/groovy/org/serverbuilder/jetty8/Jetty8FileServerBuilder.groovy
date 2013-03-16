package org.serverbuilder.jetty8

import groovy.transform.CompileStatic
import org.eclipse.jetty.server.Handler
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
class Jetty8FileServerBuilder extends SimpleJettyServerBuilder<Jetty8FileServerBuilder> implements FileServerBuilder<Server> {
    final String resourceBase = '.'
    final boolean directoriesListed = true
    final String[] welcomeFiles = []

    Jetty8FileServerBuilder() {}

    Jetty8FileServerBuilder(int port, String resourceBase, boolean directoriesListed, String... welcomeFiles) {
        super(port)
        this.resourceBase = resourceBase
        this.directoriesListed = directoriesListed
        this.welcomeFiles = welcomeFiles
    }

    Jetty8FileServerBuilder atResourceBase(String resourceBase) {
        new Jetty8FileServerBuilder(port, resourceBase, directoriesListed, welcomeFiles)
    }

    Jetty8FileServerBuilder withoutDirectoriesListed() {
        def directoriesListed = false
        new Jetty8FileServerBuilder(port, resourceBase, directoriesListed, welcomeFiles)
    }

    Jetty8FileServerBuilder withWelcomeFiles(String... welcomeFiles) {
        new Jetty8FileServerBuilder(port, resourceBase, directoriesListed, welcomeFiles)
    }

    Jetty8FileServerBuilder onPort(int port) {
        new Jetty8FileServerBuilder(port, resourceBase, directoriesListed, welcomeFiles)
    }

    @Override
    Handler handler(Server server) {
        def handler = new ResourceHandler()
        handler.with {
            it.directoriesListed = this.directoriesListed
            it.welcomeFiles = this.welcomeFiles
            it.resourceBase = this.resourceBase
        }
        handler
    }
}
