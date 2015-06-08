package com.nurkiewicz.hystrix

import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandGroupKey
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext
import spock.lang.Specification

import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.LongAdder

/**
 * Two commands aren't executed synchronously
 */
class H60_Request_caching extends Specification {

	def setup() {
		HystrixRequestContext.initializeContext()
	}

	def cleanup() {
		HystrixRequestContext.contextForCurrentThread.shutdown()
	}

	def 'should invoke cached action just once'() {
		given:
			CachedCommand one = new CachedCommand()
			CachedCommand two = new CachedCommand()
		when:
			Future<String> oneObs = one.queue()
			Future<String> twoObs = two.queue()
		and:
			oneObs.get()
			twoObs.get()
		then:
			CachedCommand.counter.intValue() == 1
	}

	class CachedCommand extends HystrixCommand<String> {

		public static final LongAdder counter = new LongAdder()

		protected CachedCommand() {
			super(HystrixCommandGroupKey.Factory.asKey("Cached"))
		}

		@Override
		protected String run() throws Exception {
			counter.increment()
			TimeUnit.MILLISECONDS.sleep(500)
			return null
		}

		@Override
		protected String getCacheKey() {
			return "1"
		}
	}

}

