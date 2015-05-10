package com.nurkiewicz.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.nurkiewicz.hystrix.examples.ExternalService;
import com.nurkiewicz.hystrix.examples.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(method = RequestMethod.GET)
class ThrottlingController {

	@Autowired
	ExternalService externalService;

	@RequestMapping("/throttling")
	String throttle(Parameters params) {
		final HystrixCommand.Setter key = HystrixCommand.Setter
				.withGroupKey(HystrixCommandGroupKey.Factory.asKey("Download"))
				.andThreadPoolPropertiesDefaults(
						HystrixThreadPoolProperties.Setter()
								.withCoreSize(4)
								.withMaxQueueSize(20)
								.withQueueSizeRejectionThreshold(15)
								.withMetricsRollingStatisticalWindowInMilliseconds(15_000)
				);

		final HystrixCommand<String> cmd = new HystrixCommand<String>(key) {

			@Override
			protected String run() throws Exception {
				return externalService.call(params);
			}
		};
		return cmd.execute();
	}

}