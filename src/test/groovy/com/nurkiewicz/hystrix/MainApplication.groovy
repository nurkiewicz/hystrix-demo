package com.nurkiewicz.hystrix

import com.codahale.metrics.MetricRegistry
import com.codahale.metrics.graphite.Graphite
import com.codahale.metrics.graphite.GraphiteReporter
import com.codahale.metrics.graphite.GraphiteSender
import com.netflix.hystrix.contrib.codahalemetricspublisher.HystrixCodaHaleMetricsPublisher
import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet
import com.netflix.hystrix.strategy.HystrixPlugins
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher
import com.nurkiewicz.hystrix.examples.ExternalService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.embedded.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import java.util.concurrent.TimeUnit

@SpringBootApplication
@RestController
@RequestMapping(method = RequestMethod.GET)
class MainApplication {

	@Autowired
	private ExternalService externalService

	public static void main(String[] args) {
		SpringApplication.run(MainApplication, args)
	}

	@Bean
	public ServletRegistrationBean statisticsStream() {
		return new ServletRegistrationBean(hystrixServlet(), "/hystrix.stream")
	}

	@Bean
	public HystrixMetricsStreamServlet hystrixServlet() {
		new HystrixMetricsStreamServlet()
	}

}

@Configuration
class Dashboard {

	@Bean
	HystrixMetricsPublisher hystrixMetricsPublisher(MetricRegistry metricRegistry) {
		HystrixCodaHaleMetricsPublisher publisher = new HystrixCodaHaleMetricsPublisher(metricRegistry);
		HystrixPlugins.getInstance().registerMetricsPublisher(publisher);
		return publisher;
	}

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
		return new Graphite(new InetSocketAddress("localhost", 2003));
	}
}