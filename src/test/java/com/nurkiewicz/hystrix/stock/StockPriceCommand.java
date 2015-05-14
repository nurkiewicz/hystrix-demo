package com.nurkiewicz.hystrix.stock;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;

public class StockPriceCommand extends HystrixCommand<StockPrice> {

    private final StockPriceGateway gateway;
    private final Ticker stock;

    public StockPriceCommand(StockPriceGateway gateway, Ticker stock) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("Stock"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("load")));
        this.gateway = gateway;
        this.stock = stock;
    }

    @Override
    protected StockPrice run() throws Exception {
        return gateway.load(stock);
    }
}