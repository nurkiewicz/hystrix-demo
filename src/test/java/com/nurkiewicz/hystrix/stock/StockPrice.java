package com.nurkiewicz.hystrix.stock;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.math.BigDecimal;
import java.time.Instant;

public class StockPrice {
	private final BigDecimal price;
	private final Instant effectiveTime;

	public StockPrice(BigDecimal price, Instant effectiveTime) {
		this.price = price;
		this.effectiveTime = effectiveTime;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Instant getEffectiveTime() {
		return effectiveTime;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("price", price)
				.add("effectiveTime", effectiveTime)
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof StockPrice)) return false;

		StockPrice that = (StockPrice) o;

		if (price != null ? !price.equals(that.price) : that.price != null) return false;
		return !(effectiveTime != null ? !effectiveTime.equals(that.effectiveTime) : that.effectiveTime != null);

	}

	@Override
	public int hashCode() {
		int result = price != null ? price.hashCode() : 0;
		result = 31 * result + (effectiveTime != null ? effectiveTime.hashCode() : 0);
		return result;
	}
}