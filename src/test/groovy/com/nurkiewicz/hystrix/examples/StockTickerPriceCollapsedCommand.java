package com.nurkiewicz.hystrix.examples;

import com.google.common.collect.ImmutableMap;
import com.netflix.hystrix.*;

import java.util.Collection;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class StockTickerPriceCollapsedCommand extends HystrixCollapser<ImmutableMap<Ticker, StockPrice>, StockPrice, Ticker> {

	private final StockPriceGateway gateway;
	private final Ticker stock;

	public StockTickerPriceCollapsedCommand(StockPriceGateway gateway, Ticker stock) {
		super(HystrixCollapser.Setter.withCollapserKey(HystrixCollapserKey.Factory.asKey("Stock"))
				.andCollapserPropertiesDefaults(
						HystrixCollapserProperties.Setter().withTimerDelayInMilliseconds(100)));
		this.gateway = gateway;
		this.stock = stock;
	}

	@Override
	public Ticker getRequestArgument() {
		return stock;
	}

	@Override
	protected HystrixCommand<ImmutableMap<Ticker, StockPrice>> createCommand(Collection<CollapsedRequest<StockPrice, Ticker>> collapsedRequests) {
		final Set<Ticker> stocks = collapsedRequests.stream()
				.map(CollapsedRequest::getArgument)
				.collect(toSet());
		return new StockPricesBatchCommand(gateway, stocks);
	}

	@Override
	protected void mapResponseToRequests(ImmutableMap<Ticker, StockPrice> batchResponse, Collection<CollapsedRequest<StockPrice, Ticker>> collapsedRequests) {
		collapsedRequests.forEach(request -> {
			final Ticker ticker = request.getArgument();
			final StockPrice price = batchResponse.get(ticker);
			request.setResponse(price);
		});
	}

}

class StockPricesBatchCommand extends HystrixCommand<ImmutableMap<Ticker, StockPrice>> {

	private final StockPriceGateway gateway;
	private final Set<Ticker> stocks;

	StockPricesBatchCommand(StockPriceGateway gateway, Set<Ticker> stocks) {
		super(HystrixCommandGroupKey.Factory.asKey("Stock"));
		this.gateway = gateway;
		this.stocks = stocks;
	}

	@Override
	protected ImmutableMap<Ticker, StockPrice> run() throws Exception {
		return gateway.loadAll(stocks);
	}
}