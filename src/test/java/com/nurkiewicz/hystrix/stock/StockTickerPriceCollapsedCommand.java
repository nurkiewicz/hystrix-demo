package com.nurkiewicz.hystrix.stock;

import com.netflix.hystrix.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

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
										.withMaxRequestsInBatch(50)
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

class StockPricesBatchCommand extends HystrixCommand<Map<Ticker, StockPrice>> {

	private final StockPriceGateway gateway;
	private final Collection<Ticker> stocks;

	StockPricesBatchCommand(StockPriceGateway gateway, Collection<Ticker> stocks) {
		super(HystrixCommandGroupKey.Factory.asKey("Stock"));
		this.gateway = gateway;
		this.stocks = stocks;
	}

	@Override
	protected Map<Ticker, StockPrice> run() throws Exception {
		return gateway.loadAll(stocks);
	}
}