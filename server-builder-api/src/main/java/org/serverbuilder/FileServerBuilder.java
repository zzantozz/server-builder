package org.serverbuilder;

public interface FileServerBuilder<ServerType> extends ServerBuilder<ServerType, FileServerBuilder<ServerType>> {
    public FileServerBuilder<ServerType> atResourceBase(String resourceBase);

    public FileServerBuilder<ServerType> withoutDirectoriesListed();

    public FileServerBuilder<ServerType> withWelcomeFiles(String... welcomeFiles);
}
