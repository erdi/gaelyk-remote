# gaelyk-remote

This project is a [Gaelyk](http://gaelyk.org)-compatible version of [groovy-remote](http://http://groovy.codehaus.org/modules/remote/). It's main usage is to setup a Gaelyk application for functional tests by allowing to execute closures defined in tests on the server. See [groovy-remote](http://http://groovy.codehaus.org/modules/remote/) project pages for genral usage and examples not specific to Gaelyk.

## Setup

The library provides a servlet that should be used as the endpoint for sending the code to be executed. All you need to do is to register this servlet in `web.xml` file of your project typically at the `/remote-control` path:

	<servlet>
        <servlet-name>RemoteControlServlet</servlet-name>
        <servlet-class>groovyx.gaelyk.remote.RemoteControlServlet</servlet-class>
    </servlet>
    
    <servlet-mapping>
        <servlet-name>RemoteControlServlet</servlet-name>
        <url-pattern>/remote-control</url-pattern>
    </servlet-mapping>
    
## Usage

Given that you've registered `groovyx.gaelyk.remote.RemoteControlServlet` at `/remote-control` and you run your development server on port `8080` for functional tests the following specification should pass:

	import groovyx.remote.client.RemoteControl
	import groovyx.remote.transport.http.HttpTransport
	import spock.lang.Specification
	
	class RemoteControlSpec extends Specification {

		private remote(Closure closure) {
			new RemoteControl(new HttpTransport('http://localhost:8080/remote-control')).exec(closure)
		}
	
		void "smoke test"() {
			expect:
			remote { 1 + 1 } == 2
		}
	}
	
## Gaelyk specific variables in remote context

To make it easier to setup your application for your tests closures passing through `groovyx.gaelyk.remote.RemoteControlServlet` get all the GAE-specific variables from [this list](http://gaelyk.appspot.com/tutorial/views-and-controllers#lazy) bound into their context. Thanks to that you can use the same property names to access the variables that are also available in your Gaelyk views and controllers. The only non GAE-specific variable added to the context of the remotely executed closures is `context` which points to the `ServletContext` instance for the application.