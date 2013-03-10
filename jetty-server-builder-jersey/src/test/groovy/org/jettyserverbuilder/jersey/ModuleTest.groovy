package org.jettyserverbuilder.jersey
import org.eclipse.jetty.server.Server
import org.junit.After
import org.junit.Test
import org.springframework.context.ApplicationContext
import org.springframework.context.support.FileSystemXmlApplicationContext

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.Application
/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/9/13
 * Time: 6:14 PM
 */
class ModuleTest {
    static class TestApplication extends Application {
        Set getClasses() { [TestResource] }
    }

    @Path('/builderTest')
    static class TestResource {
        @GET
        @Path('getter')
        String sayHi() { 'Hi from the JAX-RS resource!' }
    }

    Server thisTestServer

    @After
    void tearDown() {
        thisTestServer?.stop()
    }

    @Test
    void 'supports a typical JAX-RS Application class'() {
        verify JerseyBuilder.newJerseyServer(TestApplication)
    }

    @Test
    void 'supports typical jersey-spring applications'() {
        File springContextFile = testContextFile()
        verify JerseyBuilder.newJerseyServer("file:$springContextFile.absolutePath")
    }

    @Test
    void 'supports creating a jersey-spring application with an existing ApplicationContext'() {
        ApplicationContext context = new FileSystemXmlApplicationContext(testContextFile().absolutePath)
        verify JerseyBuilder.newJerseyServer(context)
    }

    void verify(JerseyBuilder builder) {
        def resource = builder.jerseyResource()
        thisTestServer = builder.server()
        assert resource.path('builderTest/getter').get(String) == 'Hi from the JAX-RS resource!'
    }

    def testContextFile() {
        File.createTempFile('jetty-server-builder-jersey-test-', '.xml').with {
            text = """\
            <beans xmlns="http://www.springframework.org/schema/beans"
                   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                   xsi:schemaLocation="
                       http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.2.xsd">
                <bean class="org.jettyserverbuilder.jersey.ModuleTest.TestResource" scope="singleton"/>
            </beans>
            """.stripIndent()
            return it
        }
    }
}
