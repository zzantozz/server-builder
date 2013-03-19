package org.serverbuilder

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/18/13
 * Time: 9:32 PM
 */
class ServerBuildersLoader {
    static final String IMPLEMENTATION_CLASS_PROPERTY_NAME = 'ServerBuilders.ImplementationClass'
    private static final String STANDARD_EXCEPTION_PREFIX = "Unable to locate ServerBuilders implementation to test"

    static ServerBuilders loadServerBuildersForTest() {
        String className = getPropertyValue()
        Class<?> clazz = loadImplementationClass(className)
        Object instance = instantiateImplementationClass(clazz)
        castToServerBuilders(instance)
    }

    static String getPropertyValue() {
        def className = System.getProperty(IMPLEMENTATION_CLASS_PROPERTY_NAME)
        if (!className) {
            reportError()
            throw new IllegalStateException("$STANDARD_EXCEPTION_PREFIX: system property '$IMPLEMENTATION_CLASS_PROPERTY_NAME' wasn't set")
        }
        className
    }

    static Class<?> loadImplementationClass(String className) {
        try {
            Class.forName(className)
        } catch (e) {
            reportError()
            throw new IllegalStateException("$STANDARD_EXCEPTION_PREFIX: failed to load class '$className'", e)
        }
    }

    static Object instantiateImplementationClass(Class<?> clazz) {
        try {
            clazz.newInstance()
        } catch (e) {
            reportError()
            throw new IllegalStateException("$STANDARD_EXCEPTION_PREFIX: failed to invoke default constructor of $clazz", e)
        }
    }

    static ServerBuilders castToServerBuilders(instance) {
        try {
            instance as ServerBuilders
        } catch (e) {
            throw new IllegalStateException("$STANDARD_EXCEPTION_PREFIX: provided class doesn't implement ${ServerBuilders.class.name}", e)
        }
    }

    private static void reportError() {
        System.err.println("""\
            These tests find the ServerBuilders implementation to use via system property.
            Set the '$IMPLEMENTATION_CLASS_PROPERTY_NAME' system property to the fully-qualified
            class name of the implementation of org.serverbuilder.ServerBuilders in the module
            under test. The implementation must have a public, no-arg constructor.
            """.stripIndent())
    }
}
