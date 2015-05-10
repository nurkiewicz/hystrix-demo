package com.nurkiewicz.hystrix

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet
import com.nurkiewicz.hystrix.examples.ExternalService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.embedded.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

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


