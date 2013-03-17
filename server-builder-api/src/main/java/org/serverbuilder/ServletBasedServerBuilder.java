package org.serverbuilder;

public interface ServletBasedServerBuilder<ServerType, BuilderType extends ServletBasedServerBuilder<ServerType, BuilderType>> extends ServerBuilder<ServerType, BuilderType> {
    BuilderType atContextPath(String contextPath);
    BuilderType atRootContextPath();
    BuilderType mappedTo(String urlPattern);
}
