package org.serverbuilder

import groovy.transform.CompileStatic

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/14/13
 * Time: 11:39 PM
 */
@CompileStatic
interface ServerBuilders<ServerType> {
    FileServerBuilder<ServerType> newFileServer()
    JerseyServerBuilder<ServerType> newJerseyServer()
    SpringMvcServerBuilder<ServerType> newSpringMvcServer()
}
