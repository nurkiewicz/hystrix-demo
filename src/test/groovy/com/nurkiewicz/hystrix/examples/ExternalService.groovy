package com.nurkiewicz.hystrix.examples

import groovy.transform.Canonical
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Service

import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit


@Slf4j
@Service
class ExternalService {

	private final Semaphore throttler = new Semaphore(10)

	String call(Parameters params) {
		throttler.acquire()
		try {
			return businessLogic(params)
		} finally {
			throttler.release()
		}
	}

	private String businessLogic(Parameters params) {
		log.debug("Calling with $params")
		sleepRandomly(params)
		if (Math.random() < params.failureProbability) {
			throw new RuntimeException("Simulated, don't panic")
		}
		return "OK"
	}

	private void sleepRandomly(Parameters params) {
		double realDuration = params.randomDuration()
		TimeUnit.MILLISECONDS.sleep((long) realDuration)
	}

}

@Canonical
class Parameters {

	private static final Random random = new Random()

	long duration
	double standardDev
	double failureProbability

	double randomDuration() {
		return this.duration + random.nextGaussian() * this.standardDev
	}
}
