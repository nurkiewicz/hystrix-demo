package com.nurkiewicz.hystrix.stock;

import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCollapserKey;
import com.netflix.hystrix.HystrixCollapserProperties;
import com.netflix.hystrix.HystrixCommand;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class StockTickerPriceCollapsedCommand extends HystrixCollapser<Map<Ticker, StockPrice>, StockPrice, Ticker> {

	private final StockPriceGateway gateway;
	private final Ticker stock;

	public StockTickerPriceCollapsedCommand(StockPriceGateway gateway, Ticker stock) {
		super(
				HystrixCollapser.Setter.withCollapserKey(
						HystrixCollapserKey.Factory.asKey("Stock"))
						.andCollapserPropertiesDefaults(
								HystrixCollapserProperties.Setter()
										.withTimerDelayInMilliseconds(100)
										.withMaxRequestsInBatch(500)
						)
						.andScope(Scope.GLOBAL));
		this.gateway = gateway;
		this.stock = stock;
	}

	@Override
	public Ticker getRequestArgument() {
		return stock;
	}

	@Override
	protected HystrixCommand<Map<Ticker, StockPrice>> createCommand(Collection<CollapsedRequest<StockPrice, Ticker>> collapsedRequests) {
		final List<Ticker> stocks = collapsedRequests.stream()
				.map(CollapsedRequest::getArgument)
				.collect(toList());
		return new StockPricesBatchCommand(gateway, stocks);
	}

	@Override
	protected void mapResponseToRequests(Map<Ticker, StockPrice> batchResponse, Collection<CollapsedRequest<StockPrice, Ticker>> collapsedRequests) {
		collapsedRequests.forEach(request -> {
			final Ticker ticker = request.getArgument();
			final StockPrice price = batchResponse.get(ticker);
			request.setResponse(price);
		});
	}

}

