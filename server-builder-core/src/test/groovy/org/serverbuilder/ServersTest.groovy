package org.serverbuilder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

import java.lang.reflect.Method

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/18/13
 * Time: 11:00 PM
 */
@RunWith(Parameterized)
class ServersTest {
    Method factoryMethod

    ServersTest(Method factoryMethod) {
        this.factoryMethod = factoryMethod
    }

    @Test
    void 'factory methods return ServerBuilders'() {
        println "Method: $factoryMethod"
        factoryMethod.invoke(null) instanceof ServerBuilders
    }

    @Parameterized.Parameters
    static Collection<Object[]> factoryMethods() {
        Servers.declaredMethods.collect{ [it] as Object[] }
    }
}
