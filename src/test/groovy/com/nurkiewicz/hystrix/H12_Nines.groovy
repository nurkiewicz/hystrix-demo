package com.nurkiewicz.hystrix

import org.apache.commons.lang.time.DurationFormatUtils
import spock.lang.Specification
import spock.lang.Unroll

import java.time.Duration

import static org.apache.commons.lang.time.DateUtils.MILLIS_PER_DAY

/**
 * Downtime grows exponentially with independent services
 */
@Unroll
class H12_Nines extends Specification {

	def 'Uptime of 99.99% when #serviceCount services means #downtimeHms of downtime daily'() {
		given:
			double independentUptime = 99.99 / 100
			double systemUptime = Math.pow(independentUptime, serviceCount)

		expect:
			systemUptime * 100 <= expectedMaxUptime
			secondsDowntimeDaily(systemUptime) <= expectedDowntimeSecondsDaily

		where:
			serviceCount | expectedMaxUptime | expectedDowntimeSecondsDaily
			1            | 99.99             | 8
			2            | 99.99             | 17
			5            | 99.96             | 43
			10           | 99.91             | 86
			20           | 99.81             | 172
			50           | 99.51             | 430
			100          | 99.01             | 859
			downtimeHms = Duration.ofSeconds(expectedDowntimeSecondsDaily).toString()
	}

	private long secondsDowntimeDaily(double uptime) {
		double downtime = 1 - uptime
		return downtime * MILLIS_PER_DAY / 1000
	}

}

