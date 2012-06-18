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

package groovyx.gaelyk.remote

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import com.google.appengine.api.utils.SystemProperty

import groovyx.gaelyk.GaelykBindingEnhancer
import javax.servlet.ServletConfig
import javax.servlet.ServletContext

class RemoteControlServlet extends groovyx.remote.transport.http.RemoteControlServlet {

	private ServletContext context

	@Override
	protected boolean validateRequest(HttpServletRequest request, HttpServletResponse response) {
		boolean valid = super.validateRequest(request, response)
		if (valid && SystemProperty.environment.value() != SystemProperty.Environment.Value.Development) {
			response.status = HttpURLConnection.HTTP_NOT_FOUND
			valid = false
		}
		valid
	}

	@Override
	protected groovyx.remote.server.Receiver createReceiver() {
		def binding = new Binding()
		GaelykBindingEnhancer.bind(binding)
		new Receiver([context: context] + binding.variables)
	}

	@Override
	void init(ServletConfig config) {
		context = config.servletContext
		super.init(config)
	}
}
