/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.proxerd.gaelyk.remote

import static com.google.appengine.api.utils.SystemProperty.Environment.Value.*
import static java.net.HttpURLConnection.*
import com.google.appengine.api.utils.SystemProperty
import com.meterware.httpunit.PostMethodWebRequest
import com.meterware.servletunit.ServletRunner
import com.meterware.servletunit.ServletUnitClient
import groovyx.remote.server.Receiver
import groovyx.remote.transport.http.ContentType
import spock.lang.Specification
import spock.lang.Unroll

class RemoteControlServletSpec extends Specification {
	@Unroll('Returns #responseCode when running in #environment environment')
	void "returns appropriate HTTP response code for production and development"() {
		given:
		ServletRunner runner = new ServletRunner()
		runner.registerServlet('remote-control', TestRemoteControlServlet.name)
		ServletUnitClient client = runner.newClient()
		client.exceptionsThrownOnErrorStatus = false

		when:
		SystemProperty.environment.set(environment)

		then:
		client.getResponse(new RemoteControlWebRequest()).responseCode == responseCode

		where:
		environment | responseCode
		Production | HTTP_NOT_FOUND
		Development | HTTP_OK
	}
}

class TestRemoteControlServlet extends RemoteControlServlet {
	@Override
	protected Receiver createReceiver() {
		new Receiver() {
			void execute(InputStream command, OutputStream result) {
			}
		}
	}
}

class RemoteControlWebRequest extends PostMethodWebRequest {
	RemoteControlWebRequest(String url) {
		super('http://localhost/remote-control')
	}

	@Override
	protected String getContentType() {
		ContentType.COMMAND.value
	}
}
