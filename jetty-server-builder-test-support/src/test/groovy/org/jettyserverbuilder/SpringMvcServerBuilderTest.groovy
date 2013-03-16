package org.jettyserverbuilder

import com.sun.jersey.api.client.Client
import org.eclipse.jetty.server.Server
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext
import org.springframework.web.servlet.config.annotation.EnableWebMvc

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/9/13
 * Time: 9:05 AM
 */
class SpringMvcServerBuilderTest {
    @Controller
    static class TestController {
        TestService service

        @RequestMapping('/sayHi')
        @ResponseBody String sayHi() { service.sayHi() }
    }

    @Service
    static class TestService {
        String sayHi() { 'Service says hi!' }
    }

    @Configuration
    static class RootBeans {
        @Bean TestService service() { new TestService() }
    }

    @Configuration
    @EnableWebMvc
    static class DispatcherBeans {
        @Bean TestController controller(TestService service) { new TestController([service: service]) }
    }

    WebApplicationContext onlyRootBeans = new AnnotationConfigWebApplicationContext().with {
        register RootBeans
        return it
    }

    WebApplicationContext onlyDispatcherBeans = new AnnotationConfigWebApplicationContext().with {
        register DispatcherBeans
        return it
    }

    WebApplicationContext dispatcherAndRootBeans = new AnnotationConfigWebApplicationContext().with {
        register RootBeans, DispatcherBeans
        return it
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none()
    Server thisTestServer

    @After
    void tearDown() {
        thisTestServer?.stop()
    }

    @Test
    void 'a bare-minimum builder creates a working server'() {
        verify Jetty8SpringMvcBuilder.newSpringMvcServer(dispatcherAndRootBeans)
    }

    @Test
    void 'supports typical parent/child spring mvc apps'() {
        verify Jetty8SpringMvcBuilder.newSpringMvcServer(onlyDispatcherBeans)
                .withRootContext(onlyRootBeans)
    }

    @Test
    void 'supports spring mvc apps without a root context'() {
        verify Jetty8SpringMvcBuilder.newSpringMvcServer(dispatcherAndRootBeans)
    }

    @Test
    void 'supports running on specific ports'() {
        def builder = Jetty8SpringMvcBuilder.newSpringMvcServer(dispatcherAndRootBeans).onPort(10101)
        thisTestServer = builder.server()
        assert Client.create().resource('http://localhost:10101/sayHi').get(String) == 'Service says hi!'
    }

    @Test
    void 'supports running with a specific context path'() {
        def builder = Jetty8SpringMvcBuilder.newSpringMvcServer(dispatcherAndRootBeans).atContextPath('/testContextPath/path2')
        thisTestServer = builder.server()
        assert Client.create().resource("http://localhost:$builder.port/testContextPath/path2/sayHi").get(String) ==
                'Service says hi!'
    }

    @Test
    void 'fails on setting a context path without a leading slash'() {
        expectedException.expect(IllegalArgumentException)
        Jetty8SpringMvcBuilder.newSpringMvcServer(dispatcherAndRootBeans).atContextPath('noLeadingSlash')
    }

    @Test
    void 'supports running at the root context'() {
        verify Jetty8SpringMvcBuilder.newSpringMvcServer(dispatcherAndRootBeans).atRootContextPath()
    }

    @Test
    void 'supports running at a specific servlet path'() {
        def builder = Jetty8SpringMvcBuilder.newSpringMvcServer(dispatcherAndRootBeans).mappedTo('/testServletPath/path2/*')
        thisTestServer = builder.server()
        assert Client.create().resource("http://localhost:$builder.port/testServletPath/path2/sayHi").get(String) ==
                'Service says hi!'
    }

    @Test
    void 'fails on setting a servlet path without a leading slash'() {
        expectedException.expect(IllegalArgumentException)
        Jetty8SpringMvcBuilder.newSpringMvcServer(dispatcherAndRootBeans).mappedTo('noLeadingSlash')
    }

    void verify(Jetty8SpringMvcBuilder builder) {
        thisTestServer = builder.server()
        def response = builder.jerseyResource().path('sayHi').get(String)
        assert response == 'Service says hi!'
    }
}
