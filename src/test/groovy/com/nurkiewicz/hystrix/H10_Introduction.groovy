package com.nurkiewicz.hystrix

import org.apache.commons.io.IOUtils
import spock.lang.Specification

import java.nio.charset.StandardCharsets

/**
 * Unsafe code:
 * - broken URL
 * - slow DNS
 * - broken SSL certificate
 * - slow network connection
 * - server replying slowly or never
 *
 */
class H10_Introduction extends Specification {

	def 'unsafe code'() {
		given:
			URL url = "http://www.example.com".toURL()
			InputStream input = url.openStream()

		when:
			String string = IOUtils.toString(input, StandardCharsets.UTF_8)

		then:
			!string.empty
	}

}
