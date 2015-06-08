package com.nurkiewicz.hystrix

import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandGroupKey
import org.apache.commons.io.IOUtils
import spock.lang.Specification

import java.nio.charset.StandardCharsets

class H21_Timeouts extends Specification {

	def 'Minimal Hystrix API'() {
		given:
			HystrixCommand<String> command = new TimeoutDownloadCommand()

		expect:
			String result = command.execute()

	}

	static class TimeoutDownloadCommand extends HystrixCommand<String> {

		protected TimeoutDownloadCommand() {
			super(HystrixCommandGroupKey.Factory.asKey("Download"), 5_000)
		}

		@Override
		protected String run() throws Exception {
			URL url = "http://www.example.com".toURL()
			InputStream input = url.openStream()
			IOUtils.toString(input, StandardCharsets.UTF_8)
		}
	}

}

