package org.serverbuilder.jetty8;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.serverbuilder.ServerBuilder;

public abstract class SimpleServletBasedJettyServerBuilder<BuilderType extends ServerBuilder<Server, BuilderType>> extends SimpleJettyServerBuilder<BuilderType> {
    public SimpleServletBasedJettyServerBuilder(String contextPath, String urlPattern) {
        this.contextPath = contextPath;
        this.urlPattern = urlPattern;
    }

    public SimpleServletBasedJettyServerBuilder(String contextPath) {
        this(contextPath, "/*");
    }

    public SimpleServletBasedJettyServerBuilder() {
        this("/", "/*");
    }

    public SimpleServletBasedJettyServerBuilder(int port, String contextPath, String urlPattern) {
        super(port);
        this.contextPath = contextPath;
        this.urlPattern = urlPattern;
    }

    public SimpleServletBasedJettyServerBuilder(int port, String contextPath) {
        this(port, contextPath, "/*");
    }

    public SimpleServletBasedJettyServerBuilder(int port) {
        this(port, "/", "/*");
    }

    @Override
    public Handler handler(Server server) {
        ServletContextHandler handler = new ServletContextHandler();
        handler.setContextPath(getContextPath());
        handler.addServlet(servletHolder(handler), getUrlPattern());
        return handler;
    }

    public ServletHolder servletHolder(ServletContextHandler handler) {
        return new ServletHolder();
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    private final String contextPath;
    private final String urlPattern;
}
