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

import groovyx.remote.CommandChain
import groovyx.remote.Command

class CommandChainInvoker extends groovyx.remote.server.CommandChainInvoker {
	CommandChainInvoker(ClassLoader parentLoader, CommandChain commandChain) {
		super(parentLoader, commandChain)
	}

	@Override
	protected createInvoker(ClassLoader loader, Command command) {
		return new CommandInvoker(loader, command)
	}
}