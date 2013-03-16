package org.serverbuilder
import com.sun.jersey.api.client.Client
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
class SpringMvcServerBuilderTest<T> {
    @Controller
    static class TestController {
        TestService service

        @RequestMapping('/sayHi')
        @ResponseBody String sayHi() { service.sayHi() }
    }

    @Service
    @SuppressWarnings("GrMethodMayBeStatic")
    static class TestService {
        String sayHi() { 'Service says hi!' }
    }

    @Configuration
    @SuppressWarnings("GrMethodMayBeStatic")
    static class RootBeans {
        @Bean TestService service() { new TestService() }
    }

    @Configuration
    @EnableWebMvc
    @SuppressWarnings("GrMethodMayBeStatic")
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
    SpringMvcServerBuilder<T> builder
    T thisTestServer

    SpringMvcServerBuilderTest(ServerBuilders<T> builders) {
        this.builder = builders.newSpringMvcServer()
        assert this.builder, "A builder is required to run tests against"
    }

    @After
    void tearDown() {
        if (thisTestServer) builder?.stop(thisTestServer)
    }

    @Test
    void 'a bare-minimum builder creates a working server'() {
        verify builder.withDispatcherContext(dispatcherAndRootBeans)
    }

    @Test
    void 'supports typical parent-child spring mvc apps'() {
        verify builder.withDispatcherContext(onlyDispatcherBeans)
                .withRootContext(onlyRootBeans)
    }

    @Test
    void 'supports spring mvc apps without a root context'() {
        verify builder.withDispatcherContext(dispatcherAndRootBeans)
    }

    @Test
    void 'supports running on specific ports'() {
        def builder = builder.withDispatcherContext(dispatcherAndRootBeans).onPort(10101)
        thisTestServer = builder.build()
        assert Client.create().resource('http://localhost:10101/sayHi').get(String) == 'Service says hi!'
    }

    @Test
    void 'supports running with a specific context path'() {
        def builder = builder.withDispatcherContext(dispatcherAndRootBeans).atContextPath('/testContextPath/path2')
        thisTestServer = builder.build()
        assert Client.create().resource("http://localhost:$builder.port/testContextPath/path2/sayHi").get(String) ==
                'Service says hi!'
    }

    @Test
    void 'fails on setting a context path without a leading slash'() {
        expectedException.expect(IllegalArgumentException)
        builder.withDispatcherContext(dispatcherAndRootBeans).atContextPath('noLeadingSlash')
    }

    @Test
    void 'supports running at the root context'() {
        verify builder.withDispatcherContext(dispatcherAndRootBeans).atRootContextPath()
    }

    @Test
    void 'supports running at a specific servlet path'() {
        def builder = builder.withDispatcherContext(dispatcherAndRootBeans).mappedTo('/testServletPath/path2/*')
        thisTestServer = builder.build()
        assert Client.create().resource("http://localhost:$builder.port/testServletPath/path2/sayHi").get(String) ==
                'Service says hi!'
    }

    @Test
    void 'fails on setting a servlet path without a leading slash'() {
        expectedException.expect(IllegalArgumentException)
        builder.withDispatcherContext(dispatcherAndRootBeans).mappedTo('noLeadingSlash')
    }

    void verify(SpringMvcServerBuilder<T> builder) {
        thisTestServer = builder.build()
        def response = builder.jerseyResource().path('sayHi').get(String)
        assert response == 'Service says hi!'
    }
}
