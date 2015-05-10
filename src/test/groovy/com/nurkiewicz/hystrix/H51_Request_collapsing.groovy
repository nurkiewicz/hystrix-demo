package com.nurkiewicz.hystrix

import com.netflix.hystrix.HystrixCollapser
import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandGroupKey
import spock.lang.Specification
import com.netflix.hystrix.HystrixCollapser.CollapsedRequest


/**
 * If the exact same commands are executed shortly after each other
 * Hystrix can collapse them.
 * This redueces network load, but increases latency (waiting)
 */
class H51_Request_collapsing extends Specification {

}

class Ping extends HystrixCommand<Integer> {

	protected Ping() {
		super(HystrixCommandGroupKey.Factory.asKey("Ping"))
	}

	@Override
	protected Integer run() throws Exception {
		return 42
	}
}

class PingCollapser extends HystrixCollapser<Integer, Integer, Void> {

	@Override
	Void getRequestArgument() {
		return null;
	}

	@Override
	protected HystrixCommand<Integer> createCommand(Collection<CollapsedRequest<Integer, Void>> collection) {
		return new Ping()
	}

	@Override
	protected void mapResponseToRequests(Integer integer, Collection<CollapsedRequest<Integer, Void>> batchedResponse) {
		batchedResponse.each {
			it.response = integer
		}
	}
}