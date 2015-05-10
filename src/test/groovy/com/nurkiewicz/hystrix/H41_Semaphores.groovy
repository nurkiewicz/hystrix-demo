package com.nurkiewicz.hystrix

import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandGroupKey
import com.netflix.hystrix.HystrixCommandKey
import com.netflix.hystrix.HystrixCommandProperties
import com.netflix.hystrix.HystrixThreadPoolProperties
import groovy.util.logging.Slf4j
import spock.lang.Specification
import com.netflix.hystrix.HystrixCommand.Setter


/**
 * Used when request volume is really big
 * Thread pool overhead is intolerable
 * Use only when underlying action is known to be fast
 * ...or encapsulate other Hystrix command
 */
class H41_Semaphores extends Specification {

	def 'semaphores provide smaller overhead'() {
		given:
			SemaphoreCommand command = new SemaphoreCommand()
		when:
			String result = command.execute()
		then:
			Thread.currentThread().name == result
	}

}

