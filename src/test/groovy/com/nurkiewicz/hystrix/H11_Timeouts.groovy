package com.nurkiewicz.hystrix

import org.apache.commons.io.IOUtils
import spock.lang.Specification

import java.nio.charset.StandardCharsets
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

/**
 * Timeouts for insecure code
 * - Limit max duration
 * - You always pay the price of slow query
 * - Circuit breaking
 */
class H11_Timeouts extends Specification {

    def 'manual timeouts'() {
        given:
            ExecutorService pool = Executors.newFixedThreadPool(10)

        when:
            Future<String> future = pool.submit(new DownloadTask())

        then:
            future.get(1, TimeUnit.SECONDS)
    }

}

class DownloadTask implements Callable<String> {

    @Override
    String call() throws Exception {
        URL url = "http://www.google.com".toURL()
        InputStream input = url.openStream()
        IOUtils.toString(input, StandardCharsets.UTF_8)
    }
}
