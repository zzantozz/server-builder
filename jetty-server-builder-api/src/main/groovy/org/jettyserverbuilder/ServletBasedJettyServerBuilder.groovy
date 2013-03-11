package org.jettyserverbuilder

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 3/10/13
 * Time: 7:02 PM
 */
interface ServletBasedJettyServerBuilder extends JettyServerBuilder {
    String getContextPath()
    String getUrlPattern()
}
