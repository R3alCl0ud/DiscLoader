package io.discloader.discloader.network.rest;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.mashape.unirest.http.HttpResponse;

public class RateLimiter {

	private boolean globalLimit = false;
	private long resetTime;
	private int remaining;
	private int rateLimit;
	private long resetAfter;
	private long timeDifference;
	private String route;
	private RESTQueue queue;

	public RateLimiter(RESTQueue restQueue, String url) {
		queue = restQueue;
		route = url;
	}

	public boolean shouldRateLimit(HttpResponse<String> response) {
		checkHeaders(response.getHeaders());
		return globalLimit || shouldWait() || response.getStatus() == 429;
	}

	public boolean hitGlobalLimit() {
		return globalLimit;
	}

	public boolean shouldWait() {
		return (remaining == 0 && getWaitTime() > 0);
	}

	public void checkHeaders(Map<String, List<String>> headers) {
		resetAfter = 0;
		headers.forEach((name, value) -> {
			switch (name) {
			case "X-RateLimit-Limit":
				rateLimit = Integer.parseInt(value.get(0), 10);
				break;
			case "X-RateLimit-Remaining":
				remaining = Integer.parseInt(value.get(0), 10);
				break;
			case "x-ratelimit-reset":
			case "X-RateLimit-Reset":
				resetTime = (Long.parseLong(value.get(0), 10) * 1000L);
				break;
			case "X-RateLimit-Global":
				globalLimit = Boolean.parseBoolean(value.get(0));
				break;
			case "Date":
				timeDifference = DateTime.now().toDate().getTime() - DateTime.parse(value.get(0)).toDate().getTime();
				break;
			case "retry after":
				resetAfter = (Long.parseLong(value.get(0), 10) + 500);
				break;
			}
		});
	}

	public long getWaitTime() {
		return ((resetTime - System.currentTimeMillis()) + timeDifference + 500);
	}

	public void limitRoute() {

	}

}
