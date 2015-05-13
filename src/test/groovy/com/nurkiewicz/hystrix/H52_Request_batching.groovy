package com.nurkiewicz.hystrix

import com.google.common.collect.ImmutableMap
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext
import com.nurkiewicz.hystrix.stock.StockPrice
import com.nurkiewicz.hystrix.stock.StockPriceGateway
import com.nurkiewicz.hystrix.stock.StockTickerPriceCollapsedCommand
import com.nurkiewicz.hystrix.stock.Ticker
import spock.lang.Specification

import java.time.Instant
import java.util.concurrent.Future

import static com.nurkiewicz.hystrix.Examples.ANY_STOCK_PRICE
import static com.nurkiewicz.hystrix.Examples.ANY_TICKER
import static com.nurkiewicz.hystrix.Examples.OTHER_STOCK_PRICE
import static com.nurkiewicz.hystrix.Examples.OTHER_TICKER

/**
 * Stock price example
 * @see com.nurkiewicz.hystrix.examples.StockPriceCommand first
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