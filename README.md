# Gaelyk Remote Control [![Build Status](https://buildhive.cloudbees.com/job/erdi/job/gaelyk-remote/badge/icon)](https://buildhive.cloudbees.com/job/erdi/job/gaelyk-remote/)

This project is a [Gaelyk](http://gaelyk.org)-compatible version of [Groovy Remote Control](http://http://groovy.codehaus.org/modules/remote/). It's main usage is to setup a Gaelyk application for functional tests by allowing to execute closures defined in tests on the server. See [groovy-remote](http://http://groovy.codehaus.org/modules/remote/) project pages for general usage and examples not specific to Gaelyk.

## Setup

To use the library in your Gaelyk project you first need to add the dependency in your Gradle build file:

	repositories {
		mavenCentral()
	}

	dependencies {
		runtime "org.gaelyk:gaelyk-remote:0.1"
	}

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

The following example shows how easy it is to clear the datastore after each fixture in a specification thanks to the availability of `datastore` variable in the remote closure context.

	import groovyx.remote.client.RemoteControl
	import groovyx.remote.transport.http.HttpTransport
	import spock.lang.Specification

	class RemoteControlSpec extends Specification {
		@Shared def remote = new RemoteControl(new HttpTransport("http://localhost:8080/remote-control"))

		def cleanup() {
			remote.exec { datastore.iterate { select keys }.each { it.key.delete() } }
		}
	}
	
## Security

A posibility to run arbitrary code on server is an obvious security risk. The library is aimed at being used during functional testing and that's why `groovyx.gaelyk.remote.RemoteControlServlet` checks if the application is running in development enviroment (`SystemProperty.environment.value() == SystemProperty.Environment.Value.Development`) and if that isn't the case always returns HTPP `404` error code and the remote closure is not executed.