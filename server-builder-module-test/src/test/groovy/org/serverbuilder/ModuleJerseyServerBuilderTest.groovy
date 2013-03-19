package org.serverbuilder

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/17/13
 * Time: 4:08 PM
 */
class ModuleJerseyServerBuilderTest extends JerseyServerBuilderTest {
    ModuleJerseyServerBuilderTest() {
        super(ServerBuildersLoader.loadServerBuildersForTest())
    }
}
