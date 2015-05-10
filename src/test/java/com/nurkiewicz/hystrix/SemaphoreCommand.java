package com.nurkiewicz.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;

import static com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE;

class SemaphoreCommand extends HystrixCommand<String> {

    protected SemaphoreCommand() {
        super(Setter
                        .withGroupKey(HystrixCommandGroupKey.Factory.asKey("Download"))
                        .andCommandKey(HystrixCommandKey.Factory.asKey("SemCommand"))
                        .andCommandPropertiesDefaults(
                                HystrixCommandProperties.Setter()
                                        .withExecutionIsolationStrategy(SEMAPHORE)
                                        .withExecutionIsolationSemaphoreMaxConcurrentRequests(10)
                        )
        );
    }

    @Override
    protected String run() throws Exception {
        return Thread.currentThread().getName();
    }
}