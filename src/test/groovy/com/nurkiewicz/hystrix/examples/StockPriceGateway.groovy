package com.nurkiewicz.hystrix.examples

import com.google.common.collect.ImmutableMap
import groovy.transform.Canonical
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Service

import java.time.Instant
import java.util.concurrent.TimeUnit


@Canonical
class Ticker {
	String symbol;
}

@Canonical
class StockPrice {
	BigDecimal price;
	Instant effectiveTime;
}

@Slf4j
@Service
class StockPriceGateway {

	private static Random RAND = new Random()

	StockPrice load(Ticker stock) {
		final Set<Ticker> oneTicker = Collections.singleton(stock);
		return loadAll(oneTicker).get(stock);
	}

	ImmutableMap<Ticker, StockPrice> loadAll(Set<Ticker> tickers) {
		log.info("Loading $tickers")
		randomArtificialSleep(tickers.size())
		return tickers.collect {
			[it, new StockPrice(0.0, Instant.now())]
		}.collectEntries()
	}

	private static void randomArtificialSleep(int dataSize) {
		final int millis = 100 + RAND.nextInt(10 * dataSize)
		TimeUnit.MILLISECONDS.sleep(millis)
	}
}
