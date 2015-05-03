package com.nurkiewicz.hystrix

import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandGroupKey
import com.netflix.hystrix.HystrixCommandKey
import com.netflix.hystrix.HystrixCommandProperties
import org.apache.commons.io.IOUtils
import spock.lang.Specification
import static com.netflix.hystrix.HystrixCommand.Setter

import java.nio.charset.StandardCharsets

/**
 * Circuit breaker
 * - Enabled by default
 * - Configurable volume threshold
 * - Configurable error percentage
 * - Configurable window to make one testing attempt
 */
class H30_Circuit_breaker extends Specification {

    def 'Minimal Hystric API'() {
        given:
            CircuitBreakingDownloadCommand command = new CircuitBreakingDownloadCommand()

        expect:
            String result = command.execute()

    }

}


class CircuitBreakingDownloadCommand extends HystrixCommand<String> {

    protected CircuitBreakingDownloadCommand() {
        super(
                Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("Download"))
                        .andCommandKey(HystrixCommandKey.Factory.asKey("SomeCommand"))
                        .andCommandPropertiesDefaults(
                        HystrixCommandProperties.Setter()
                                .withCircuitBreakerEnabled(true)
                                .withCircuitBreakerErrorThresholdPercentage(90)
                                .withCircuitBreakerRequestVolumeThreshold(100)
                                .withCircuitBreakerSleepWindowInMilliseconds(2_000))
        )
    }

    @Override
    protected String run() throws Exception {
        URL url = "http://www.google.com".toURL()
        InputStream input = url.openStream()
        IOUtils.toString(input, StandardCharsets.UTF_8)
    }
}