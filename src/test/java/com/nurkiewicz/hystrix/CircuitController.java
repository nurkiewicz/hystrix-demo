package com.nurkiewicz.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.nurkiewicz.hystrix.ExternalService;
import com.nurkiewicz.hystrix.QueryParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(method = RequestMethod.GET)
class CircuitController {

	@Autowired
	ExternalService externalService;

	@RequestMapping("/circuit")
	String circuit(QueryParams params) {
		HystrixCommand.Setter key = HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("Download"))
				.andCommandKey(HystrixCommandKey.Factory.asKey("SomeCommand"))
				.andCommandPropertiesDefaults(
				HystrixCommandProperties.Setter()
						.withCircuitBreakerEnabled(true)
						.withCircuitBreakerErrorThresholdPercentage(50)
						.withCircuitBreakerRequestVolumeThreshold(20)
						.withCircuitBreakerSleepWindowInMilliseconds(5_000));

		final HystrixCommand<String> cmd = new HystrixCommand<String>(key) {

			@Override
			protected String run() throws Exception {
				return externalService.call(params);
			}
		};
		return cmd.execute();
	}

}