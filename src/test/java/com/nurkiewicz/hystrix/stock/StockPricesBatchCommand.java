package com.nurkiewicz.hystrix.stock;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

import java.util.Collection;
import java.util.Map;

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