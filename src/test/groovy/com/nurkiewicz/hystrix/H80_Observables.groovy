package com.nurkiewicz.hystrix

import com.netflix.hystrix.HystrixCommand
import rx.Observable
import spock.lang.Specification


class H80_Observables extends Specification {

	def 'asynchronous command'() {
		given:
			HystrixCommand<String> command = new CustomDownloadCommand(url)
		when:
			Observable<String> obs = command.observe()
		then:
			Observable<Integer> result = obs.map { x -> x.length() }
			result.toBlocking().first() == 42
	}

	def 'compose asynchronous commands'() {
		given:
			CustomDownloadCommand google = new CustomDownloadCommand("http://www.google.com".toURL())
			CustomDownloadCommand bing = new CustomDownloadCommand("http://www.bing.com".toURL())
			CustomDownloadCommand nurkiewicz = new CustomDownloadCommand("http://www.nurkiewicz.com".toURL())
		and:
			Observable<String> googleObs = google.observe()
			Observable<String> bingObs = bing.observe()
			Observable<String> nurkiewiczObs = nurkiewicz.observe()
		when:
			Observable<String> allResults = Observable.merge(googleObs, bingObs, nurkiewiczObs)
		then:
			Observable<List<String>> resultsList = allResults
					.filter{html -> html.contains('2015')}
					.toList()
			println(googleObs.toBlocking().single())
			resultsList.toBlocking().forEach{println(it)}
	}

}