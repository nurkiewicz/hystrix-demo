package com.nurkiewicz.hystrix

import org.apache.commons.io.IOUtils
import spock.lang.Specification

import java.nio.charset.StandardCharsets
import java.util.concurrent.*

/**
 * Timeouts for unsafe code
 * - Limit max duration
 * - You always pay the price of slow query / fail fast
 * - Circuit breaking
 * - Missing metrics: avg time, queue length
 */
class H11_Timeouts extends Specification {

	def 'manual timeouts'() {
		given:
			ExecutorService pool = Executors.newFixedThreadPool(10)
		when:
			Future<String> future = pool.submit(new DownloadTask())
		then:
			future.get(1, TimeUnit.SECONDS)
		cleanup:
			pool.shutdownNow()
	}

	static class DownloadTask implements Callable<String> {

		@Override
		String call() throws Exception {
			URL url = "http://www.example.com".toURL()
			InputStream input = url.openStream()
			IOUtils.toString(input, StandardCharsets.UTF_8)
		}
	}


}

