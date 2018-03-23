package io.discloader.discloader.network.rest;

import java.util.List;
import java.util.Map;

public class RateLimiter {

	private long resetTime;
	private int remaining;
	private int rateLimit;
	private long resetAfter;
	private long timeDifference;
	private Route<?> route;

	public RateLimiter(Route<?> route) {
		resetTime = 0;
		remaining = 1;
		rateLimit = 0;
		resetAfter = 0l;
		timeDifference = 0l;
		this.route = route;
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
				route.getREST().setGlobally(Boolean.parseBoolean(value.get(0)));
				break;
			case "Date":
				break;
			case "Retry-After":
				resetAfter = (Long.parseLong(value.get(0), 10) + 500);
				break;
			default:
				break;
			}
		});
	}

	public boolean isLimited() {
		return route.getREST().isGlobally() || !hasRemaining();
	}

	public int getLimit() {
		return rateLimit;
	}

	public boolean hasRemaining() {
		return remaining > 0;
	}

	public long retryIn() {
		if (route.getREST().isGlobally())
			return resetAfter;
		return resetTime - System.currentTimeMillis() + 500;
	}

	public long waitTime() {
		return ((resetTime - System.currentTimeMillis()) + timeDifference + 500);
	}

	public void limitRoute(boolean resetGlobal, long sleepTime) {
		Thread limit = new Thread(String.format("Limiting %s", route.toString())) {
			@Override
			public void run() {
				try {
					sleep(sleepTime);
					remaining = 1;
					route.handle();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		limit.setPriority((Thread.NORM_PRIORITY + Thread.MAX_PRIORITY) / 2);
		limit.setDaemon(true);
		limit.start();
	}

}
