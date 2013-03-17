package org.serverbuilder.jetty8;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.serverbuilder.FileServerBuilder;

public class Jetty8FileServerBuilder extends SimpleJettyServerBuilder<FileServerBuilder<Server>> implements FileServerBuilder<Server> {
    private String resourceBase = ".";
    private boolean directoriesListed = true;
    private String[] welcomeFiles = new String[0];

    public Jetty8FileServerBuilder() {
    }

    public Jetty8FileServerBuilder(int port, String resourceBase, boolean directoriesListed, String... welcomeFiles) {
        super(port);
        this.resourceBase = resourceBase;
        this.directoriesListed = directoriesListed;
        this.welcomeFiles = welcomeFiles;
    }

    public Jetty8FileServerBuilder atResourceBase(String resourceBase) {
        return new Jetty8FileServerBuilder(getPort(), resourceBase, directoriesListed, welcomeFiles);
    }

    public Jetty8FileServerBuilder withoutDirectoriesListed() {
        Boolean directoriesListed = false;
        return new Jetty8FileServerBuilder(getPort(), resourceBase, directoriesListed, welcomeFiles);
    }

    public Jetty8FileServerBuilder withWelcomeFiles(String... welcomeFiles) {
        return new Jetty8FileServerBuilder(getPort(), resourceBase, directoriesListed, welcomeFiles);
    }

    public Jetty8FileServerBuilder onPort(int port) {
        return new Jetty8FileServerBuilder(port, resourceBase, directoriesListed, welcomeFiles);
    }

    @Override
    public Handler handler(Server server) {
        ResourceHandler handler = new ResourceHandler();
        handler.setDirectoriesListed(this.directoriesListed);
        handler.setWelcomeFiles(this.welcomeFiles);
        handler.setResourceBase(this.resourceBase);
        return handler;
    }
}
