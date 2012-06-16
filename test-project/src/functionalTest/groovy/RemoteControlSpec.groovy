import groovyx.gaelyk.GaelykBindingEnhancer
import groovyx.remote.client.RemoteControl
import groovyx.remote.client.RemoteException
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

	void "remote exceptions are handled without errors"() {
		when:
		remote { throw new Exception('test message') }

		then:
		def remoteException = thrown(RemoteException)
		remoteException.cause.message == 'test message'
	}

	void "all gaelyk services and variables are available in the remote context"() {
		given:
		def checkContextVariables = {
			def binding = new Binding()
			def context = delegate
			GaelykBindingEnhancer.bind(binding)

			binding.variables.collectEntries { key, value ->
				def contextValue = context[key]
				def available = (contextValue == value) || (contextValue.getClass() == value.getClass())
				[key, available]
			}
		}

		expect:
		remote(checkContextVariables).every { key, value -> value }
	}
}
