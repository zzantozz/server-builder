package org.jettyserverbuilder

import com.sun.jersey.api.client.Client
import org.eclipse.jetty.server.Server
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
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
class JerseyServerBuilderTest {
    static class TestApplication extends Application {
        Set getClasses() { [TestResource] }
    }

    @Path('/builderTest')
    static class TestResource {
        @GET
        @Path('getter')
        String sayHi() { 'Hi from the JAX-RS resource!' }
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none()
    Server thisTestServer

    @After
    void tearDown() {
        thisTestServer?.stop()
    }

    @Test
    void 'supports a typical JAX-RS Application class'() {
        verify Jetty8JerseyServerBuilder.newJerseyServer(TestApplication)
    }

    @Test
    void 'supports typical jersey-spring applications'() {
        File springContextFile = testContextFile()
        verify Jetty8JerseyServerBuilder.newJerseyServer("file:$springContextFile.absolutePath")
    }

    @Test
    void 'supports creating a jersey-spring application with an existing ApplicationContext'() {
        ApplicationContext context = new FileSystemXmlApplicationContext(testContextFile().absolutePath)
        verify Jetty8JerseyServerBuilder.newJerseyServer(context)
    }

    @Test
    void 'supports running on specific ports'() {
        def builder = Jetty8JerseyServerBuilder.newJerseyServer(TestApplication).onPort(10101)
        thisTestServer = builder.server()
        assert Client.create().resource('http://localhost:10101/builderTest/getter').get(String) ==
                'Hi from the JAX-RS resource!'
    }

    @Test
    void 'supports running with a specific context path'() {
        def builder = Jetty8JerseyServerBuilder.newJerseyServer(TestApplication).atContextPath('/testContextPath/path2')
        thisTestServer = builder.server()
        assert Client.create().resource("http://localhost:$builder.port/testContextPath/path2/builderTest/getter").get(String) ==
                'Hi from the JAX-RS resource!'
    }

    @Test
    void 'fails on setting a context path without a leading slash'() {
        expectedException.expect(IllegalArgumentException)
        Jetty8JerseyServerBuilder.newJerseyServer(TestApplication).atContextPath('noLeadingSlash')
    }

    @Test
    void 'supports running at the root context'() {
        verify Jetty8JerseyServerBuilder.newJerseyServer(TestApplication).atRootContextPath()
    }

    @Test
    void 'supports running at a specific servlet path'() {
        def builder = Jetty8JerseyServerBuilder.newJerseyServer(TestApplication).mappedTo('/testServletPath/path2/*')
        thisTestServer = builder.server()
        assert Client.create().resource("http://localhost:$builder.port/testServletPath/path2/builderTest/getter").get(String) ==
                'Hi from the JAX-RS resource!'
    }

    @Test
    void 'fails on setting a servlet path without a leading slash'() {
        expectedException.expect(IllegalArgumentException)
        Jetty8JerseyServerBuilder.newJerseyServer(TestApplication).mappedTo('noLeadingSlash')
    }

    void verify(Jetty8JerseyServerBuilder builder) {
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
