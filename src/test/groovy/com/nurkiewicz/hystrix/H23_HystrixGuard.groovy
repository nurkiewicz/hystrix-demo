package com.nurkiewicz.hystrix

import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandGroupKey
import com.nurkiewicz.hystrix.examples.ExternalService
import com.nurkiewicz.hystrix.examples.Parameters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(method = RequestMethod.GET)
class H23_HystrixGuard {

	@Autowired
	private ExternalService externalService

	@RequestMapping("/safe")
	String safe(Parameters params) {
		HystrixCommand.Setter key = HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("External"))
		return new HystrixCommand<String>(key) {
			@Override
			protected String run() throws Exception {
				unsafe(params)
			}
		}.execute()
	}

	@RequestMapping("/unsafe")
	String unsafe(Parameters params) {
		externalService.call(params)
		return "OK"
	}


}


