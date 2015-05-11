package com.nurkiewicz.hystrix.examples;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

class StockPriceCommand extends HystrixCommand<StockPrice> {

    private final StockPriceGateway gateway;
    private final Ticker stock;

    StockPriceCommand(StockPriceGateway gateway, Ticker stock) {
        super(HystrixCommandGroupKey.Factory.asKey("Stock"));
        this.gateway = gateway;
        this.stock = stock;
    }

    @Override
    protected StockPrice run() throws Exception {
        return gateway.load(stock);
    }
}