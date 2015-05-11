package com.nurkiewicz.hystrix

import com.google.common.collect.ImmutableMap
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext
import com.nurkiewicz.hystrix.examples.StockPrice
import com.nurkiewicz.hystrix.examples.StockPriceGateway
import com.nurkiewicz.hystrix.examples.StockTickerPriceCollapsedCommand
import com.nurkiewicz.hystrix.examples.Ticker
import groovy.transform.Canonical
import groovy.util.logging.Slf4j
import spock.lang.Specification

import java.time.Instant
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

import static com.nurkiewicz.hystrix.Examples.getANY_STOCK_PRICE
import static com.nurkiewicz.hystrix.Examples.getANY_STOCK_PRICE
import static com.nurkiewicz.hystrix.Examples.getANY_TICKER
import static com.nurkiewicz.hystrix.Examples.getANY_TICKER
import static com.nurkiewicz.hystrix.Examples.getANY_TICKER
import static com.nurkiewicz.hystrix.Examples.getANY_TICKER
import static com.nurkiewicz.hystrix.Examples.getANY_TICKER
import static com.nurkiewicz.hystrix.Examples.getANY_TICKER
import static com.nurkiewicz.hystrix.Examples.getANY_TICKER
import static com.nurkiewicz.hystrix.Examples.getOTHER_STOCK_PRICE
import static com.nurkiewicz.hystrix.Examples.getOTHER_TICKER
import static com.nurkiewicz.hystrix.Examples.getOTHER_TICKER
import static com.nurkiewicz.hystrix.Examples.getOTHER_TICKER

/**
 * Stock price example
 */
class H52_Request_batching extends Specification {
	def setup() {
		HystrixRequestContext.initializeContext()
	}

	def cleanup() {
		HystrixRequestContext.contextForCurrentThread.shutdown()
	}

	def 'should collapse two commands executed concurrently for the same stock ticker'() {
		given:
			def gateway = Mock(StockPriceGateway)
			def tickers = [ANY_TICKER] as Set

		and:
			def commandOne = new StockTickerPriceCollapsedCommand(gateway, ANY_TICKER)
			def commandTwo = new StockTickerPriceCollapsedCommand(gateway, ANY_TICKER)

		when:
			Future<StockPrice> futureOne = commandOne.queue()
			Future<StockPrice> futureTwo = commandTwo.queue()

		and:
			futureOne.get()
			futureTwo.get()

		then:
			0 * gateway.load(_)
			1 * gateway.loadAll(tickers) >> ImmutableMap.of(ANY_TICKER, ANY_STOCK_PRICE)
	}

	def 'should collapse two commands executed concurrently for the different stock tickers'() {
		given:
			def gateway = Mock(StockPriceGateway)
			def tickers = [ANY_TICKER, OTHER_TICKER] as Set

		and:
			def commandOne = new StockTickerPriceCollapsedCommand(gateway, ANY_TICKER)
			def commandTwo = new StockTickerPriceCollapsedCommand(gateway, OTHER_TICKER)

		when:
			Future<StockPrice> futureOne = commandOne.queue()
			Future<StockPrice> futureTwo = commandTwo.queue()

		and:
			futureOne.get()
			futureTwo.get()

		then:
			1 * gateway.loadAll(tickers) >> ImmutableMap.of(
					ANY_TICKER, ANY_STOCK_PRICE,
					OTHER_TICKER, OTHER_STOCK_PRICE)
	}

}

class Examples {

	static final Ticker ANY_TICKER = new Ticker("IBM")
	static final StockPrice ANY_STOCK_PRICE = new StockPrice(BigDecimal.TEN, Instant.now())

	static final Ticker OTHER_TICKER = new Ticker("MSFT")
	static final StockPrice OTHER_STOCK_PRICE = new StockPrice(BigDecimal.ONE, Instant.now())

}