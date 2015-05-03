package com.nurkiewicz.hystrix

import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandGroupKey
import org.apache.commons.io.IOUtils
import spock.lang.Specification

import java.nio.charset.StandardCharsets

/**
 * Configuring thread pool size
 */
class H21_Timeouts extends Specification {

    def 'Minimal Hystric API'() {
        given:
            CircuitBreakingDownloadCommand command = new CircuitBreakingDownloadCommand()

        expect:
            String result = command.execute()

    }

}

class TimeoutDownloadCommand extends HystrixCommand<String> {

    protected TimeoutDownloadCommand() {
        super(HystrixCommandGroupKey.Factory.asKey("Download"), 5_000)
    }

    @Override
    protected String run() throws Exception {
        URL url = "http://www.google.com".toURL()
        InputStream input = url.openStream()
        IOUtils.toString(input, StandardCharsets.UTF_8)
    }
}