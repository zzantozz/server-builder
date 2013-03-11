package org.jettyserverbuilder

import org.eclipse.jetty.server.Handler
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.ResourceHandler
/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/10/13
 * Time: 6:11 PM
 */
class FileServerBuilder extends SimpleJettyServerBuilder {
    final String resourceBase
    final boolean directoriesListed = true
    final String[] welcomeFiles = []

    FileServerBuilder(String resourceBase) {
        this.resourceBase = resourceBase
    }

    FileServerBuilder(int port, String resourceBase, boolean directoriesListed, String... welcomeFiles) {
        super(port)
        this.resourceBase = resourceBase
        this.directoriesListed = directoriesListed
        this.welcomeFiles = welcomeFiles
    }

    static FileServerBuilder newFileServer(String resourceBase) {
        new FileServerBuilder(resourceBase)
    }

    FileServerBuilder withoutDirectoriesListed() {
        def directoriesListed = false
        new FileServerBuilder(port, resourceBase, directoriesListed, welcomeFiles)
    }

    FileServerBuilder withWelcomeFiles(String... welcomeFiles) {
        new FileServerBuilder(port, resourceBase, directoriesListed, welcomeFiles)
    }

    FileServerBuilder onPort(int port) {
        new FileServerBuilder(port, resourceBase, directoriesListed, welcomeFiles)
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
