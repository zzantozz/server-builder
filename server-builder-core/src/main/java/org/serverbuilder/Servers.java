package org.serverbuilder;

import org.eclipse.jetty.server.Server;
import org.serverbuilder.jetty7.Jetty7ServerBuilders;
import org.serverbuilder.jetty8.Jetty8ServerBuilders;

public class Servers {
    public static ServerBuilders<Server> jetty7() {
        return new Jetty7ServerBuilders();
    }

    public static ServerBuilders<Server> jetty8() {
        return new Jetty8ServerBuilders();
    }
}
