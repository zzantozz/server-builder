package org.serverbuilder

import groovy.transform.CompileStatic

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/10/13
 * Time: 7:02 PM
 */
@CompileStatic
interface ServletBasedServerBuilder<ServerType, BuilderType extends
        ServletBasedServerBuilder<ServerType, BuilderType>> extends ServerBuilder<ServerType, BuilderType> {
    BuilderType atContextPath(String contextPath)
    BuilderType atRootContextPath()
    BuilderType mappedTo(String urlPattern)
}
