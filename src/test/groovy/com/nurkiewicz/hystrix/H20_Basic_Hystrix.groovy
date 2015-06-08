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
			HystrixCommand<String> command = new BasicDownloadCommand()

		expect:
			String result = command.execute()

	}

	static class BasicDownloadCommand extends HystrixCommand<String> {

		protected BasicDownloadCommand() {
			super(HystrixCommandGroupKey.Factory.asKey("Download"))
		}

		@Override
		protected String run() throws Exception {
			URL url = "http://www.example.com".toURL()
			InputStream input = url.openStream()
			return IOUtils.toString(input, StandardCharsets.UTF_8)
		}
	}

}

