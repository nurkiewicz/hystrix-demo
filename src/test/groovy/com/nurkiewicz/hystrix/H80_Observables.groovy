package com.nurkiewicz.hystrix

import com.netflix.hystrix.HystrixCommand
import rx.Observable
import spock.lang.Specification


class H80_Observables extends Specification {

	def 'asynchronous command'() {
		given:
			HystrixCommand<String> command = new CustomDownloadCommand("http://www.example.com".toURL())
		when:
			Observable<String> obs = command.observe()
		then:
			Observable<Integer> result = obs.map { x -> x.length() }
			result.toBlocking().single() == 42
	}

	def 'compose asynchronous commands'() {
		given:
			CustomDownloadCommand example = new CustomDownloadCommand("http://www.example.com".toURL())
			CustomDownloadCommand bing = new CustomDownloadCommand("http://www.bing.com".toURL())
			CustomDownloadCommand nurkiewicz = new CustomDownloadCommand("http://www.nurkiewicz.com".toURL())
		and:
			Observable<String> exampleObs = example.observe()
			Observable<String> bingObs = bing.observe()
			Observable<String> nurkiewiczObs = nurkiewicz.observe()
		when:
			Observable<String> allResults = Observable.merge(exampleObs, bingObs, nurkiewiczObs)
		then:
			Observable<List<String>> resultsList = allResults
					.filter{html -> html.contains('2015')}
					.toList()
			resultsList.toBlocking().forEach{println(it)}
	}

}