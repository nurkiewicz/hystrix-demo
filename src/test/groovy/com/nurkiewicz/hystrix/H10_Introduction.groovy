package com.nurkiewicz.hystrix

import org.apache.commons.io.IOUtils
import spock.lang.Specification

import java.nio.charset.StandardCharsets

/**
 * Insecure code:
 * - broken URL
 * - slow DNS
 * - broken SSL certificate
 * - slow network connection
 * - server replying slowly or never
 *
 */
class H10_Introduction extends Specification {

	def 'insecure code'() {
		given:
			URL url = "http://www.google.com".toURL()
			InputStream input = url.openStream()

		expect:
			IOUtils.toString(input, StandardCharsets.UTF_8)
	}

}
