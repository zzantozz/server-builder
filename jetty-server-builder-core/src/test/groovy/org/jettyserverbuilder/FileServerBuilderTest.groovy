package org.jettyserverbuilder
import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.ClientResponse
import org.eclipse.jetty.server.Server
import org.junit.After
import org.junit.Test
/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/10/13
 * Time: 6:10 PM
 */
class FileServerBuilderTest {
    File baseDirectory = File.createTempFile("jetty-server-builder-file-test-", null).with {
        if(!(it.delete() && it.mkdir())) {
            throw new IllegalStateException("Failed to obtain a temporary directory")
        }
        return it
    }
    File testFile1 = new File(baseDirectory, 'foo').with {
        text = 'content of foo'
        return it
    }
    File testFile2 = new File(baseDirectory, 'bar').with {
        text = 'content of bar'
        return it
    }

    Server thisTestServer

    @After
    void tearDown() {
        thisTestServer?.stop()
    }

    @Test
    void 'directory listings can be disabled'() {
        def builder = FileServerBuilder.newFileServer(baseDirectory.absolutePath).withoutDirectoriesListed()
        thisTestServer = builder.server()
        assert builder.jerseyResource().get(ClientResponse).status == 403
    }

    @Test
    void 'directory listings are enabled by default'() {
        def builder = FileServerBuilder.newFileServer(baseDirectory.absolutePath)
        thisTestServer = builder.server()
        def pageContent = builder.jerseyResource().get(String)
        assert pageContent =~ 'Directory:'
        assert pageContent =~ 'foo'
        assert pageContent =~ 'bar'
        assert pageContent =~ 'bytes'
    }

    @Test
    void 'a static file can be served'() {
        def builder = FileServerBuilder.newFileServer(baseDirectory.absolutePath)
        thisTestServer = builder.server()
        def client = builder.jerseyResource()
        assert client.path('foo').get(String) == 'content of foo'
        assert client.path('bar').get(String) == 'content of bar'
    }

    @Test
    void 'welcome files can be set'() {
        def builder = FileServerBuilder.newFileServer(baseDirectory.absolutePath).withWelcomeFiles('foo')
        thisTestServer = builder.server()
        assert builder.jerseyResource().get(String) == 'content of foo'
    }

    @Test
    void "returns 404 when file isn't found"() {
        def builder = FileServerBuilder.newFileServer(baseDirectory.absolutePath)
        thisTestServer = builder.server()
        assert builder.jerseyResource().path('nonexistent').get(ClientResponse).status == 404
    }

    @Test
    void 'supports running on specific ports'() {
        def builder = FileServerBuilder.newFileServer(baseDirectory.absolutePath).onPort(10101)
        thisTestServer = builder.server()
        assert Client.create().resource('http://localhost:10101/foo').get(String) == 'content of foo'
    }
}
