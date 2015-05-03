package com.nurkiewicz.hystrix

import com.netflix.hystrix.HystrixCommandGroupKey
import com.netflix.hystrix.HystrixObservableCommand
import rx.Observable
import spock.lang.Specification


class H81_ObservableCommand extends Specification {

}

class ObservableDownloadCommand extends HystrixObservableCommand<String> {

    protected ObservableDownloadCommand() {
        super(HystrixCommandGroupKey.Factory.asKey("Download"))
    }

    @Override
    protected Observable<String> construct() {
        return Observable.just("Content")
    }
}