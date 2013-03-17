package org.serverbuilder.jetty8;

import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.serverbuilder.JerseyServerBuilder;
import org.serverbuilder.SpringMvcServerBuilder;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class Jetty8SpringMvcBuilder extends SimpleServletBasedJettyServerBuilder<Jetty8SpringMvcBuilder> implements SpringMvcServerBuilder<Server> {
    public Jetty8SpringMvcBuilder() {
    }

    public Jetty8SpringMvcBuilder(int port, String contextPath, String urlPattern, WebApplicationContext rootContext, WebApplicationContext dispatcherContext) {
        super(port, contextPath, urlPattern);
        this.rootContext = rootContext;
        this.dispatcherContext = dispatcherContext;
    }

    public Jetty8SpringMvcBuilder withDispatcherContext(WebApplicationContext dispatcherContext) {
        return new Jetty8SpringMvcBuilder(getPort(), getContextPath(), getUrlPattern(), rootContext, dispatcherContext);
    }

    public Jetty8SpringMvcBuilder withRootContext(WebApplicationContext rootContext) {
        return new Jetty8SpringMvcBuilder(getPort(), getContextPath(), getUrlPattern(), rootContext, dispatcherContext);
    }

    public Jetty8SpringMvcBuilder atContextPath(String contextPath) {
        if (!contextPath.startsWith("/")) {
            throw new IllegalArgumentException("Context path must start with a '/', was " + contextPath);
        }

        return new Jetty8SpringMvcBuilder(getPort(), contextPath, getUrlPattern(), rootContext, dispatcherContext);
    }

    public Jetty8SpringMvcBuilder atRootContextPath() {
        String contextPath = "/";
        return new Jetty8SpringMvcBuilder(getPort(), contextPath, getUrlPattern(), rootContext, dispatcherContext);
    }

    public Jetty8SpringMvcBuilder mappedTo(String urlPattern) {
        if (!urlPattern.startsWith("/")) {
            throw new IllegalArgumentException("A servlet mapping must start with a '/', was " + urlPattern);
        }

        return new Jetty8SpringMvcBuilder(getPort(), getContextPath(), urlPattern, rootContext, dispatcherContext);
    }

    public Jetty8SpringMvcBuilder onPort(int port) {
        return new Jetty8SpringMvcBuilder(port, getContextPath(), getUrlPattern(), rootContext, dispatcherContext);
    }

    @Override
    public ServletHolder servletHolder(ServletContextHandler handler) {
        if (DefaultGroovyMethods.asBoolean(rootContext))
            handler.addEventListener(new ContextLoaderListener(rootContext));
        return new ServletHolder(new DispatcherServlet(dispatcherContext));
    }

    public WebApplicationContext getRootContext() {
        return rootContext;
    }

    public WebApplicationContext getDispatcherContext() {
        return dispatcherContext;
    }

    private WebApplicationContext rootContext;
    private WebApplicationContext dispatcherContext;
}
