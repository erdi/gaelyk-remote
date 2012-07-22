ruleset {
	ruleset('rulesets/basic.xml')
	ruleset('rulesets/braces.xml')
	ruleset('rulesets/concurrency.xml')
	ruleset('rulesets/convention.xml')
	ruleset('rulesets/design.xml')
	ruleset('rulesets/dry.xml')
	ruleset('rulesets/exceptions.xml') {
		ThrowException {
			doNotApplyToClassNames = 'groovyx.gaelyk.remote.RemoteControlSpec'
		}
	}
	ruleset('rulesets/formatting.xml')
	ruleset('rulesets/generic.xml')
	ruleset('rulesets/grails.xml')
	ruleset('rulesets/groovyism.xml')
	ruleset('rulesets/imports.xml') {
		MisorderedStaticImports {
			comesBefore = false
		}
	}
	ruleset('rulesets/jdbc.xml')
	ruleset('rulesets/junit.xml')
	ruleset('rulesets/logging.xml')
	ruleset('rulesets/naming.xml') {
		MethodName {
			regex = /[a-z][\w\s]*/
		}
		FactoryMethodName {
			doNotApplyToClassNames = 'groovyx.gaelyk.remote.Receiver'
		}
	}
	ruleset('rulesets/security.xml')
	ruleset('rulesets/serialization.xml')
	ruleset('rulesets/size.xml')
	ruleset('rulesets/unnecessary.xml')
	ruleset('rulesets/unused.xml')
}