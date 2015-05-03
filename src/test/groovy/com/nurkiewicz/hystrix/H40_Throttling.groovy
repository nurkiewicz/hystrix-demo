package com.nurkiewicz.hystrix

import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandGroupKey
import com.netflix.hystrix.HystrixThreadPoolProperties
import spock.lang.Specification
import static com.netflix.hystrix.HystrixCommand.Setter

/**
 * Thread pool and queue size
 */
class H40_Throttling extends Specification {

    def 'customized pool and queue size'() {
        given:
            PooledCommand command = new PooledCommand()
        expect:
            command.execute()
    }

}

class PooledCommand extends HystrixCommand<String> {

    protected PooledCommand() {
        super(Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("Download"))
                .andThreadPoolPropertiesDefaults(
                HystrixThreadPoolProperties.Setter()
                        .withCoreSize(4)
                        .withMaxQueueSize(20)
                        .withQueueSizeRejectionThreshold(15)
                        .withMetricsRollingStatisticalWindowInMilliseconds(15_000)
        ))
    }

    @Override
    protected String run() throws Exception {
        return null
    }
}