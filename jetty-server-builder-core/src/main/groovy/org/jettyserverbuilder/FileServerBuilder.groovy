package org.jettyserverbuilder

import groovy.transform.TupleConstructor
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.ResourceHandler
/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/10/13
 * Time: 6:11 PM
 */
@TupleConstructor
class FileServerBuilder extends AbstractJettyServerBuilder {
    final int port = 8080
    final String resourceBase
    final boolean directoriesListed = true
    final String[] welcomeFiles = []

    static FileServerBuilder newFileServer(String resourceBase) {
        new FileServerBuilder().withResourceBase(resourceBase)
    }

    FileServerBuilder withResourceBase(String resourceBase) {
        new FileServerBuilder(port, resourceBase, directoriesListed, welcomeFiles)
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
    Server server() {
        def resourceHandler = new ResourceHandler()
        resourceHandler.with {
            it.directoriesListed = this.directoriesListed
            it.welcomeFiles = this.welcomeFiles
            it.resourceBase = this.resourceBase
        }
        def server = new Server(port)
        server.handler = resourceHandler
        server.start()
        server
    }
}
