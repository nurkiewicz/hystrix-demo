package com.nurkiewicz.hystrix.stock;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toMap;

@Service
class SimulatedStockPriceGateway implements StockPriceGateway {

	private static final Logger log = LoggerFactory.getLogger(SimulatedStockPriceGateway.class);
	private static Random RAND = new Random();
	private final Counter requests;
	private final Histogram requestSize;
	private final Histogram inputSize;

	@Autowired
	public SimulatedStockPriceGateway(MetricRegistry metricRegistry) {
		requests = metricRegistry.counter(SimulatedStockPriceGateway.class.getSimpleName() + ".requests");
		inputSize = metricRegistry.histogram(SimulatedStockPriceGateway.class.getSimpleName() + ".inputSize");
		requestSize = metricRegistry.histogram(SimulatedStockPriceGateway.class.getSimpleName() + ".requestSize");
	}

	@Override
	public StockPrice load(Ticker ticker) {
		log.debug("Loading {}", ticker);
		final Set<Ticker> oneTicker = Collections.singleton(ticker);
		return loadMany(oneTicker).get(ticker);
	}

	@Override
	public Map<Ticker, StockPrice> loadMany(Collection<Ticker> tickers) {
		final HashSet<Ticker> uniqueTickers = new HashSet<>(tickers);
		log.debug("Loading batch of ({}, unique: {}): {}", tickers.size(), uniqueTickers.size(), uniqueTickers);
		requests.inc();
		inputSize.update(tickers.size());
		requestSize.update(uniqueTickers.size());
		randomArtificialSleep(uniqueTickers.size());
		return uniqueTickers
				.stream()
				.collect(toMap(t -> t, t -> new StockPrice(BigDecimal.ONE, Instant.now())));
	}

	private static void randomArtificialSleep(int dataSize) {
		try {
			final int millis = 10 + RAND.nextInt(10 * dataSize);
			TimeUnit.MILLISECONDS.sleep(millis);
		} catch (InterruptedException ignored) {
		}
	}
}