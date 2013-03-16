package org.jettyserverbuilder

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/14/13
 * Time: 11:06 PM
 */
public interface FileServerBuilder<ServerType> extends
        ServerBuilder<ServerType, FileServerBuilder<ServerType>> {
    FileServerBuilder<ServerType> atResourceBase(String resourceBase)
    FileServerBuilder<ServerType> withoutDirectoriesListed()
    FileServerBuilder<ServerType> withWelcomeFiles(String... welcomeFiles)
}