package com.nurkiewicz.hystrix

import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandGroupKey
import com.netflix.hystrix.HystrixCommandKey
import com.netflix.hystrix.HystrixCommandProperties
import com.netflix.hystrix.HystrixThreadPoolProperties
import groovy.util.logging.Slf4j
import org.apache.commons.io.IOUtils
import spock.lang.Specification
import com.netflix.hystrix.HystrixCommand.Setter
import java.nio.charset.StandardCharsets

/**
 * Circuit breaker
 * - Enabled by default
 * - Configurable volume threshold
 * - Configurable error percentage
 * - Configurable window to make one testing attempt
 * @see com.nurkiewicz.hystrix.CircuitController
 */
class H30_Circuit_breaker extends Specification {

	def 'Minimal Hystric API'() {
		given:
			CircuitBreakingDownloadCommand command = new CircuitBreakingDownloadCommand()

		expect:
			String result = command.execute()
	}

}

@Slf4j
class CircuitBreakingDownloadCommand extends HystrixCommand<String> {

	protected CircuitBreakingDownloadCommand() {
		super(
				Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("Download"))
						.andCommandKey(HystrixCommandKey.Factory.asKey("SomeCommand"))
						.andThreadPoolPropertiesDefaults(
						HystrixThreadPoolProperties.Setter()
								.withMetricsRollingStatisticalWindowInMilliseconds(10_000))
						.andCommandPropertiesDefaults(
						HystrixCommandProperties.Setter()
								.withCircuitBreakerEnabled(true)
								.withCircuitBreakerErrorThresholdPercentage(50)
								.withCircuitBreakerRequestVolumeThreshold(20)
								.withCircuitBreakerSleepWindowInMilliseconds(5_000)
				)
		)
	}

	@Override
	protected String run() throws Exception {
		log.debug("Downloading...")
		URL url = "http://www.example.com/404".toURL()
		InputStream input = url.openStream()
		return IOUtils.toString(input, StandardCharsets.UTF_8)
	}
}