package org.jettyserverbuilder

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/10/13
 * Time: 7:02 PM
 */
interface ServletBasedServerBuilder<ServerType, BuilderType extends
        ServletBasedServerBuilder<ServerType, BuilderType>> extends ServerBuilder<ServerType, BuilderType> {
    BuilderType atContextPath(String contextPath)
    BuilderType atRootContextPath()
    BuilderType mappedTo(String urlPattern)
}
