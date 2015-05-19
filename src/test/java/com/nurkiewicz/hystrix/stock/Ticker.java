package com.nurkiewicz.hystrix.stock;

public class Ticker {
	private final String symbol;

	public Ticker(String symbol) {
		this.symbol = symbol;
	}

	public String getSymbol() {
		return symbol;
	}

	@Override
	public String toString() {
		return symbol;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Ticker)) return false;

		Ticker ticker = (Ticker) o;

		return !(symbol != null ? !symbol.equals(ticker.symbol) : ticker.symbol != null);

	}

	@Override
	public int hashCode() {
		return symbol != null ? symbol.hashCode() : 0;
	}
}





