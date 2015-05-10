package com.nurkiewicz.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

class CustomDownloadCommand extends HystrixCommand<String> {

	private final URL url;

	protected CustomDownloadCommand(URL url) {
		super(HystrixCommandGroupKey.Factory.asKey("Download"));
		this.url = url;
	}

	@Override
	protected String run() throws Exception {
		try (InputStream input = url.openStream()) {
			return IOUtils.toString(input, StandardCharsets.UTF_8);
		}
	}
}