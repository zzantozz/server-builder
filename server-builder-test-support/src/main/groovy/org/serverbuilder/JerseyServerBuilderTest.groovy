package org.serverbuilder

import com.sun.jersey.api.client.Client
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
class JerseyServerBuilderTest<T> {
    static class TestApplication extends Application {
        Set getClasses() { [TestResource] }
    }

    @Path('/builderTest')
    static class TestResource {
        @GET
        @Path('getter')
        @SuppressWarnings("GrMethodMayBeStatic")
        String sayHi() { 'Hi from the JAX-RS resource!' }
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none()
    JerseyServerBuilder<T> builder
    T thisTestServer

    JerseyServerBuilderTest(ServerBuilders builders) {
        this.builder = builders.newJerseyServer()
        assert this.builder, "A builder is required to run tests against"
    }

    @After
    void tearDown() {
        if (thisTestServer) builder?.stop(thisTestServer)
    }

    @Test
    void 'supports a typical JAX-RS Application class'() {
        verify builder.withApplicationClass(TestApplication)
    }

    @Test
    void 'supports typical jersey-spring applications'() {
        File springContextFile = testContextFile()
        verify builder.withSpringContextConfigLocation("file:$springContextFile.absolutePath")
    }

    @Test
    void 'supports creating a jersey-spring application with an existing ApplicationContext'() {
        ApplicationContext context = new FileSystemXmlApplicationContext(testContextFile().absolutePath)
        verify builder.withSpringContext(context)
    }

    @Test
    void 'supports running on specific ports'() {
        def builder = builder.withApplicationClass(TestApplication).onPort(10101)
        thisTestServer = builder.build()
        assert Client.create().resource('http://localhost:10101/builderTest/getter').get(String) ==
                'Hi from the JAX-RS resource!'
    }

    @Test
    void 'supports running with a specific context path'() {
        def builder = builder.withApplicationClass(TestApplication).atContextPath('/testContextPath/path2')
        thisTestServer = builder.build()
        def resource = Client.create().resource(builder.jerseyResource().URI)
        assert resource.path("/testContextPath/path2/builderTest/getter").get(String) == 'Hi from the JAX-RS resource!'
    }

    @Test
    void 'fails on setting a context path without a leading slash'() {
        expectedException.expect(IllegalArgumentException)
        builder.withApplicationClass(TestApplication).atContextPath('noLeadingSlash')
    }

    @Test
    void 'supports running at the root context'() {
        verify builder.withApplicationClass(TestApplication).atRootContextPath()
    }

    @Test
    void 'supports running at a specific servlet path'() {
        def builder = builder.withApplicationClass(TestApplication).mappedTo('/testServletPath/path2/*')
        thisTestServer = builder.build()
        def resource = Client.create().resource(builder.jerseyResource().URI)
        assert resource.path("/testServletPath/path2/builderTest/getter").get(String) == 'Hi from the JAX-RS resource!'
    }

    @Test
    void 'fails on setting a servlet path without a leading slash'() {
        expectedException.expect(IllegalArgumentException)
        builder.withApplicationClass(TestApplication).mappedTo('noLeadingSlash')
    }

    void verify(JerseyServerBuilder<T> builder) {
        def resource = builder.jerseyResource()
        thisTestServer = builder.build()
        assert resource.path('builderTest/getter').get(String) == 'Hi from the JAX-RS resource!'
    }

    static def testContextFile() {
        File.createTempFile('jetty-server-builder-jersey-test-', '.xml').with {
            text = """\
            <beans xmlns="http://www.springframework.org/schema/beans"
                   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                   xsi:schemaLocation="
                       http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.2.xsd">
                <bean class="org.serverbuilder.JerseyServerBuilderTest.TestResource" scope="singleton"/>
            </beans>
            """.stripIndent()
            return it
        }
    }
}
