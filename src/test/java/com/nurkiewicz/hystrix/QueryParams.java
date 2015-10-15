package com.nurkiewicz.hystrix;

import com.google.common.base.MoreObjects;

import java.util.Random;

public class QueryParams {

	private static final Random random = new Random();

	private long duration;
	private double standardDev;
	private double errorRate;

	public double randomDuration() {
		return this.duration + random.nextGaussian() * this.standardDev;
	}

	public static Random getRandom() {
		return random;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public double getStandardDev() {
		return standardDev;
	}

	public void setStandardDev(double standardDev) {
		this.standardDev = standardDev;
	}

	public double getErrorRate() {
		return errorRate;
	}

	public void setErrorRate(double errorRate) {
		this.errorRate = errorRate;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("duration", duration)
				.add("standardDev", standardDev)
				.add("errorRate", errorRate)
				.toString();
	}
}
