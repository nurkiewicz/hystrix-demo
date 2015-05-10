package com.nurkiewicz.hystrix

import spock.lang.Specification

/**
 * Thread pool and queue size
 * @see com.nurkiewicz.hystrix.ThrottlingController
 */
class H40_Throttling extends Specification {

	def 'customized pool and queue size'() {
		given:
			PooledCommand command = new PooledCommand()
		expect:
			command.execute() == null
	}

}

