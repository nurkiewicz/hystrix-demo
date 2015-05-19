package com.nurkiewicz.hystrix.stock;

import java.util.Collection;
import java.util.Map;


public interface StockPriceGateway {

	StockPrice load(Ticker ticker);

	Map<Ticker, StockPrice> loadMany(Collection<Ticker> tickers);

}