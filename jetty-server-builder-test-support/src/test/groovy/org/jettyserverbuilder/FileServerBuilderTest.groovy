package org.jettyserverbuilder
import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.ClientResponse
import org.junit.After
import org.junit.Test
/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/10/13
 * Time: 6:10 PM
 */
class FileServerBuilderTest<T> {
    File baseDirectory = File.createTempFile("file-server-builder-test-", null).with {
        if(!(it.delete() && it.mkdir())) {
            throw new IllegalStateException("Failed to obtain a temporary directory")
        }
        return it
    }
    File testFile1 = new File(baseDirectory, 'foo').with {
        text = 'content of file 1'
        return it
    }
    File testFile2 = new File(baseDirectory, 'bar').with {
        text = 'content of file 2'
        return it
    }

    FileServerBuilder<T> builder
    T thisTestServer

    FileServerBuilderTest(ServerBuilders<T> builders) {
        this.builder = builders.newFileServer()
    }

    @After
    void tearDown() {
        builder.stop(thisTestServer)
    }

    @Test
    void 'directory listings can be disabled'() {
        def builder = builder.atResourceBase(baseDirectory.absolutePath).withoutDirectoriesListed()
        thisTestServer = builder.build()
        assert builder.jerseyResource().get(ClientResponse).status == 403
    }

    @Test
    void 'directory listings are enabled by default'() {
        def builder = builder.atResourceBase(baseDirectory.absolutePath)
        thisTestServer = builder.build()
        def pageContent = builder.jerseyResource().get(String)
        assert pageContent =~ 'Directory:'
        assert pageContent =~ testFile1.name
        assert pageContent =~ testFile2.name
        assert pageContent =~ 'bytes'
    }

    @Test
    void 'a static file can be served'() {
        def builder = builder.atResourceBase(baseDirectory.absolutePath)
        thisTestServer = builder.build()
        def client = builder.jerseyResource()
        assert client.path(testFile1.name).get(String) == 'content of file 1'
        assert client.path(testFile2.name).get(String) == 'content of file 2'
    }

    @Test
    void 'welcome files can be set'() {
        def builder = builder.atResourceBase(baseDirectory.absolutePath).withWelcomeFiles(testFile1.name)
        thisTestServer = builder.build()
        assert builder.jerseyResource().get(String) == 'content of file 1'
    }

    @Test
    void "returns 404 when file isn't found"() {
        def builder = builder.atResourceBase(baseDirectory.absolutePath)
        thisTestServer = builder.build()
        assert builder.jerseyResource().path('nonexistent').get(ClientResponse).status == 404
    }

    @Test
    void 'supports running on specific ports'() {
        def builder = builder.atResourceBase(baseDirectory.absolutePath).onPort(10101)
        thisTestServer = builder.build()
        assert Client.create().resource("http://localhost:10101/$testFile1.name").get(String) == 'content of file 1'
    }
}
