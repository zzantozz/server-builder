package org.serverbuilder.jetty7;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import groovy.transform.CompileStatic;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.serverbuilder.FileServerBuilder;

@CompileStatic
public class Jetty7FileServerBuilder implements FileServerBuilder<Server> {
    @Override
    public FileServerBuilder<Server> atResourceBase(String resourceBase) {
        this.resourceBase = resourceBase;
        return this;
    }

    @Override
    public FileServerBuilder<Server> withoutDirectoriesListed() {
        this.directoriesListed = false;
        return this;
    }

    @Override
    public FileServerBuilder<Server> withWelcomeFiles(String... welcomeFiles) {
        this.welcomeFiles = welcomeFiles;
        return this;
    }

    @Override
    public FileServerBuilder<Server> onPort(int port) {
        this.port = port;
        return this;
    }

    @Override
    public WebResource jerseyResource() {
        return Client.create().resource("http://localhost:" + String.valueOf(port));
    }

    @Override
    public Server build() {
        Server server = new Server(port);
        ResourceHandler handler = new ResourceHandler();
        handler.setDirectoriesListed(this.directoriesListed);
        handler.setWelcomeFiles(this.welcomeFiles);
        handler.setResourceBase(this.resourceBase);
        server.setHandler(handler);
        try {
            server.start();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to start server", e);
        }
        return server;
    }

    @Override
    public void stop(Server server) {
        try {
            server.stop();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to stop server", e);
        }
    }

    public String getResourceBase() {
        return resourceBase;
    }

    public void setResourceBase(String resourceBase) {
        this.resourceBase = resourceBase;
    }

    public String[] getWelcomeFiles() {
        return welcomeFiles;
    }

    public void setWelcomeFiles(String[] welcomeFiles) {
        this.welcomeFiles = welcomeFiles;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean getDirectoriesListed() {
        return directoriesListed;
    }

    public boolean isDirectoriesListed() {
        return directoriesListed;
    }

    public void setDirectoriesListed(boolean directoriesListed) {
        this.directoriesListed = directoriesListed;
    }

    private String resourceBase = ".";
    private String[] welcomeFiles = new String[0];
    private int port = 8080;
    private boolean directoriesListed = true;

    private static <Value extends String> Value setResourceBase0(ResourceHandler propOwner, Value resourceBase) {
        propOwner.setResourceBase(resourceBase);
        return resourceBase;
    }
}
