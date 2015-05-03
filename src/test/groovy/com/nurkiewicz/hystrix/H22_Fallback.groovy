package com.nurkiewicz.hystrix

import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandGroupKey
import org.apache.commons.io.IOUtils
import spock.lang.Specification

import java.nio.charset.StandardCharsets

import static com.nurkiewicz.hystrix.FallbackDownloadCommand.FALLBACK

class H22_Fallback extends Specification {

    def 'Fallback command'() {
        given:
            FallbackDownloadCommand command = new FallbackDownloadCommand()
        when:
            String result = command.execute()
        then:
            result == FALLBACK
    }

}


class FallbackDownloadCommand extends HystrixCommand<String> {

    public static final String FALLBACK = "Temporarily unavailable"

    protected FallbackDownloadCommand() {
        super(HystrixCommandGroupKey.Factory.asKey("Download"))
    }

    @Override
    protected String run() throws Exception {
        URL url = "http://www.google.com/404".toURL()
        InputStream input = url.openStream()
        IOUtils.toString(input, StandardCharsets.UTF_8)
    }

    @Override
    protected String getFallback() {
        return FALLBACK
    }
}