package com.nurkiewicz.hystrix

import com.google.common.collect.ImmutableMap
import com.netflix.hystrix.HystrixCollapser
import com.netflix.hystrix.HystrixCollapserKey
import com.netflix.hystrix.HystrixCollapserProperties
import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandGroupKey
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext
import com.nurkiewicz.hystrix.examples.StockPrice
import com.nurkiewicz.hystrix.examples.StockPriceGateway
import com.nurkiewicz.hystrix.examples.StockPricesBatchCommand
import com.nurkiewicz.hystrix.examples.StockTickerPriceCollapsedCommand
import com.nurkiewicz.hystrix.examples.Ticker
import groovy.util.logging.Slf4j
import spock.lang.Specification

import java.time.Instant
import java.util.concurrent.Future

import static com.nurkiewicz.hystrix.Examples.ANY_STOCK_PRICE
import static com.nurkiewicz.hystrix.Examples.ANY_TICKER
import static com.nurkiewicz.hystrix.Examples.OTHER_STOCK_PRICE
import static com.nurkiewicz.hystrix.Examples.OTHER_TICKER
import static java.util.stream.Collectors.toSet

import com.google.common.collect.ImmutableMap;
import com.netflix.hystrix.*;

import java.util.Collection;
import java.util.Set;


/**
 * If the exact same commands are executed shortly after each other
 * Hystrix can collapse them.
 * This reduces network load, but increases latency (waiting)
 */
class H51_Request_collapsing extends Specification {

	def setup() {
		HystrixRequestContext.initializeContext()
	}

	def cleanup() {
		HystrixRequestContext.contextForCurrentThread.shutdown()
	}

	def 'should ping twice'() {

	}

}

@Slf4j
class Ping extends HystrixCommand<Pong> {

	protected Ping() {
		super(HystrixCommandGroupKey.Factory.asKey("Ping"))
	}

	@Override
	protected Pong run() throws Exception {
		log.info("Ping!")
		return new Pong();
	}
}

class Pong {}

public class PingCollapser extends HystrixCollapser<Pong, Pong, Void> {

	public PingCollapser() {
		super(HystrixCollapser.Setter.withCollapserKey(HystrixCollapserKey.Factory.asKey("Stock"))
				.andCollapserPropertiesDefaults(
				HystrixCollapserProperties.Setter().withTimerDelayInMilliseconds(100)));
	}

	@Override
	public Void getRequestArgument() {
		return stock;
	}

	@Override
	protected HystrixCommand<Pong> createCommand(Collection<com.netflix.hystrix.HystrixCollapser.CollapsedRequest<Pong, Void>> collapsedRequests) {
		return new Ping();
	}

	@Override
	protected void mapResponseToRequests(Pong batchResponse, Collection<com.netflix.hystrix.HystrixCollapser.CollapsedRequest<Pong, Void>> collapsedRequests) {
		collapsedRequests.each{request ->
			request.setResponse(batchResponse);
		};
	}

}