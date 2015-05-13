package com.nurkiewicz.hystrix

import com.netflix.hystrix.HystrixCommandGroupKey
import com.netflix.hystrix.HystrixCommandKey
import com.netflix.hystrix.HystrixObservableCommand
import rx.Observable
import spock.lang.Specification

class ObservableDownloadCommand extends HystrixObservableCommand<String> {

	protected ObservableDownloadCommand() {
		super(HystrixCommandGroupKey.Factory.asKey("Download"))
	}

	@Override
	protected Observable<String> construct() {
		return Observable
				.timer(100, TimeUnit.MILLISECONDS)
				.map{x -> 'Result text'}
	}
}

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

}
