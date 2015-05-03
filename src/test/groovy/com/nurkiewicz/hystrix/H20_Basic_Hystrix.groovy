package com.nurkiewicz.hystrix

import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandGroupKey
import org.apache.commons.io.IOUtils
import spock.lang.Specification

import java.nio.charset.StandardCharsets

/**
 * Basic API example
 * - running in a separate thread pool
 * - default timeout of one second
 */
class H20_Basic_Hystrix extends Specification {

    def 'Minimal Hystric API'() {
        given:
            CircuitBreakingDownloadCommand command = new CircuitBreakingDownloadCommand()

        expect:
            String result = command.execute()

    }

}

class BasicDownloadCommand extends HystrixCommand<String> {

    protected BasicDownloadCommand() {
        super(HystrixCommandGroupKey.Factory.asKey("Download"))
    }

    @Override
    protected String run() throws Exception {
        URL url = "http://www.google.com".toURL()
        InputStream input = url.openStream()
        IOUtils.toString(input, StandardCharsets.UTF_8)
    }
}