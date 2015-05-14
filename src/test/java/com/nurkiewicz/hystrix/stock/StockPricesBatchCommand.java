package com.nurkiewicz.hystrix.stock;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;

import java.util.Collection;
import java.util.Map;

class StockPricesBatchCommand extends HystrixCommand<Map<Ticker, StockPrice>> {

	private final StockPriceGateway gateway;
	private final Collection<Ticker> stocks;

	StockPricesBatchCommand(StockPriceGateway gateway, Collection<Ticker> stocks) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("Stock"))
				.andCommandKey(HystrixCommandKey.Factory.asKey("loadAll")));
		this.gateway = gateway;
		this.stocks = stocks;
	}

	@Override
	protected Map<Ticker, StockPrice> run() throws Exception {
		return gateway.loadMany(stocks);
	}
}