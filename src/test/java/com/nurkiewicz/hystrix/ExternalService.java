package com.nurkiewicz.hystrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Service
class ExternalService {

	private static final Logger log = LoggerFactory.getLogger(ExternalService.class);

	private final Semaphore throttler = new Semaphore(10);

	String call(QueryParams params) {
		throttlerAcquire();
		try {
			return businessLogic(params);
		} finally {
			throttler.release();
		}
	}

	private void throttlerAcquire() {
		try {
			throttler.acquire();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private String businessLogic(QueryParams params) {
		log.debug("Calling with {}", params);
		sleepRandomly(params);
		if (Math.random() < params.getFailureProbability()) {
			throw new RuntimeException("Simulated, don't panic");
		}
		return "OK";
	}

	private void sleepRandomly(QueryParams params) {
		double realDuration = params.randomDuration();
		try {
			TimeUnit.MILLISECONDS.sleep((long) realDuration);
		} catch (InterruptedException ignored) {
		}
	}

}
