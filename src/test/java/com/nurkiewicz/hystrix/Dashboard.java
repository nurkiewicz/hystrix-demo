package com.nurkiewicz.hystrix;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.graphite.GraphiteSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@Configuration
@Profile("graphite")
class Dashboard {

	private static final Logger log = LoggerFactory.getLogger(Dashboard.class);

	@Bean
	public GraphiteReporter graphiteReporter(MetricRegistry metricRegistry) {
		final GraphiteReporter reporter = GraphiteReporter
				.forRegistry(metricRegistry)
				.build(graphite());
		reporter.start(1, TimeUnit.SECONDS);
		return reporter;
	}

	@Bean
	GraphiteSender graphite() {
		InetSocketAddress address = new InetSocketAddress("localhost", 2003);
		log.info("Connecting to {}", address);
		return new Graphite(address);
	}
}