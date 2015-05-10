package com.nurkiewicz.hystrix

import com.netflix.hystrix.HystrixCommandGroupKey
import com.netflix.hystrix.HystrixObservableCommand
import rx.Observable
import spock.lang.Specification

import java.util.concurrent.TimeUnit

/**
 * Used for non-blocking, reactive commands
 */
class H81_ObservableCommand extends Specification {

	def 'observable command does not occupy threads'() {
		given:
			HystrixObservableCommand<String> command = new ObservableDownloadCommand()
		when:
			Observable<String> observe = command.observe()
		then:
			observe
					.toList()
					.toBlocking()
					.first() == ['Result text']
	}

	def 'observable command that times out'() {
		given:
			HystrixObservableCommand<String> command = new SlowCommand()
		when:
			Observable<String> observe = command.observe()
		then:
			observe
					.toList()
					.toBlocking()
					.first() == ['Alpha']
	}

}

class ObservableDownloadCommand extends HystrixObservableCommand<String> {

	protected ObservableDownloadCommand() {
		super(HystrixCommandGroupKey.Factory.asKey("Download"))
	}

	@Override
	protected Observable<String> construct() {
		return Observable.just('Result text')
	}
}

class SlowCommand extends HystrixObservableCommand<String> {

	protected SlowCommand() {
		super(HystrixCommandGroupKey.Factory.asKey("Download"))
	}

	@Override
	protected Observable<String> construct() {
		TimeUnit.SECONDS.sleep(30)
		return Observable.just("Alpha")
	}
}