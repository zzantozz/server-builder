package org.serverbuilder;

public interface ServletBasedServerBuilder<ServerType, BuilderType extends ServletBasedServerBuilder<ServerType, BuilderType>> extends ServerBuilder<ServerType, BuilderType> {
    public BuilderType atContextPath(String contextPath);

    public BuilderType atRootContextPath();

    public BuilderType mappedTo(String urlPattern);
}
