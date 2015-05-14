package com.nurkiewicz.hystrix

import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandGroupKey
import com.netflix.hystrix.HystrixCommandKey
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(method = RequestMethod.GET)
class H23_HystrixGuard {

	@Autowired
	private ExternalService externalService

	@RequestMapping("/safe")
	String safe(QueryParams params) {
		HystrixCommand.Setter key = HystrixCommand.Setter.withGroupKey(
				HystrixCommandGroupKey.Factory.asKey("External"))
				.andCommandKey(HystrixCommandKey.Factory.asKey("/safe"))
		return new HystrixCommand<String>(key) {
			@Override
			protected String run() throws Exception {
				unsafe(params)
			}
		}.execute()
	}

	@RequestMapping("/unsafe")
	String unsafe(QueryParams params) {
		externalService.call(params)
		return "OK"
	}


}


