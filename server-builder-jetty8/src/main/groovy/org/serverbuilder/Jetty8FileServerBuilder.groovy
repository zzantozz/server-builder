package org.serverbuilder

import org.eclipse.jetty.server.Handler
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.ResourceHandler
/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/10/13
 * Time: 6:11 PM
 */
class Jetty8FileServerBuilder extends SimpleJettyServerBuilder implements FileServerBuilder<Server> {
    final String resourceBase
    final boolean directoriesListed = true
    final String[] welcomeFiles = []

    Jetty8FileServerBuilder(String resourceBase) {
        this.resourceBase = resourceBase
    }

    Jetty8FileServerBuilder(int port, String resourceBase, boolean directoriesListed, String... welcomeFiles) {
        super(port)
        this.resourceBase = resourceBase
        this.directoriesListed = directoriesListed
        this.welcomeFiles = welcomeFiles
    }

    static Jetty8FileServerBuilder newFileServer(String resourceBase) {
        new Jetty8FileServerBuilder(resourceBase)
    }

    Jetty8FileServerBuilder withoutDirectoriesListed() {
        def directoriesListed = false
        new Jetty8FileServerBuilder(port, resourceBase, directoriesListed, welcomeFiles)
    }

    Jetty8FileServerBuilder withWelcomeFiles(String... welcomeFiles) {
        new Jetty8FileServerBuilder(port, resourceBase, directoriesListed, welcomeFiles)
    }

    Object onPort(int port) {
        new Jetty8FileServerBuilder(port, resourceBase, directoriesListed, welcomeFiles)
    }

    @Override
    Handler handler(Server server) {
        new ResourceHandler().with {
            it.directoriesListed = this.directoriesListed
            it.welcomeFiles = this.welcomeFiles
            it.resourceBase = this.resourceBase
            return it
        }
    }
}
