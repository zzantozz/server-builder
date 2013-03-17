package org.serverbuilder;

public interface FileServerBuilder<ServerType> extends ServerBuilder<ServerType, FileServerBuilder<ServerType>> {
    FileServerBuilder<ServerType> atResourceBase(String resourceBase);
    FileServerBuilder<ServerType> withoutDirectoriesListed();
    FileServerBuilder<ServerType> withWelcomeFiles(String... welcomeFiles);
}
