package org.serverbuilder.jetty8;

import groovy.transform.CompileStatic;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.serverbuilder.FileServerBuilder;

@CompileStatic
public class Jetty8FileServerBuilder extends SimpleJettyServerBuilder<FileServerBuilder<Server>> implements FileServerBuilder<Server> {
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

    public String getResourceBase() {
        return resourceBase;
    }

    public boolean getDirectoriesListed() {
        return directoriesListed;
    }

    public boolean isDirectoriesListed() {
        return directoriesListed;
    }

    public String[] getWelcomeFiles() {
        return welcomeFiles;
    }

    private String resourceBase = ".";
    private boolean directoriesListed = true;
    private String[] welcomeFiles = new String[0];

    private static <Value extends String> Value setResourceBase0(ResourceHandler propOwner, Value resourceBase) {
        propOwner.setResourceBase(resourceBase);
        return resourceBase;
    }
}
