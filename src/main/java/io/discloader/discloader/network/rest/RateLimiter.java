package io.discloader.discloader.network.rest;

import java.util.List;
import java.util.Map;

public class RateLimiter {

	private static boolean globalLimit = false;
	private long resetTime;
	private int remaining;
	private int rateLimit;
	private long resetAfter;
	private long timeDifference;
	private Route route;

	public RateLimiter() {
		resetTime = 0;
		remaining = 0;
		rateLimit = 0;
		resetAfter = 0l;
		timeDifference = 0l;
	}

	public void readHeaders(Map<String, List<String>> headers) {
		resetAfter = 0;
		headers.forEach((name, value) -> {
			switch (name) {
			case "X-RateLimit-Limit":
				rateLimit = Integer.parseInt(value.get(0), 10);
				break;
			case "X-RateLimit-Remaining":
				remaining = Integer.parseInt(value.get(0), 10);
				break;
			case "X-RateLimit-Reset":
				resetTime = (Long.parseLong(value.get(0), 10) * 1000L);
				break;
			case "X-RateLimit-Global":
				globalLimit = Boolean.parseBoolean(value.get(0));
				break;
			case "Date":
				break;
			case "Retry-After":
				resetAfter = (Long.parseLong(value.get(0), 10) + 500);
				break;
			}
		});
	}

	/**
	 * @return the globalLimit
	 */
	public boolean hitGlobalLimit() {
		return globalLimit;
	}

	public boolean hitLimit() {
		return globalLimit || !hasRemaining();
	}

	public int getLimit() {
		return rateLimit;
	}

	public boolean hasRemaining() {
		return remaining >= 1;
	}

	public long retryIn() {
		if (globalLimit) return resetAfter;
		return resetTime - System.currentTimeMillis() + 500;
	}

	public void limitRoute(boolean resetGlobal) {
		Thread limit = new Thread(String.format("Limiting %s", route.getRoute())) {
			
		};
	}

}
