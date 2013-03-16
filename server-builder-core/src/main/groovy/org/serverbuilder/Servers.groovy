package org.serverbuilder

import groovy.transform.CompileStatic
import org.eclipse.jetty.server.Server
import org.serverbuilder.jetty8.Jetty8ServerBuilders

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/15/13
 * Time: 7:58 PM
 */
@CompileStatic
class Servers {
    static ServerBuilders<Server> jetty7() {
        null
    }

    static ServerBuilders<Server> jetty8() {
        new Jetty8ServerBuilders()
    }
}
