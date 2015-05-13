package com.nurkiewicz.hystrix.stock;

import com.netflix.hystrix.HystrixExecutable;
import com.nurkiewicz.hystrix.examples.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping(method = RequestMethod.GET, value = "/stock")
public class StockController {

	private final StockPriceGateway gateway;

	@Autowired
	public StockController(StockPriceGateway gateway) {
		this.gateway = gateway;
	}

	@RequestMapping("/fast/{ticker}")
	public BigDecimal fast(@PathVariable("ticker") String symbol) {
		final Ticker ticker = new Ticker(symbol);
		final HystrixExecutable<StockPrice> cmd = new StockPriceCommand(gateway, ticker);
		return cmd.execute().getPrice();
	}

	@RequestMapping("/batch/{ticker}")
	public BigDecimal batch(@PathVariable("ticker") String symbol) {
		final Ticker ticker = new Ticker(symbol);
		final HystrixExecutable<StockPrice> cmd = new StockTickerPriceCollapsedCommand(gateway, ticker);
		return cmd.execute().getPrice();
	}

}
