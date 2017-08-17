package io.discloader.discloader.network.rest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Perry Berman
 *
 */
public class RequestHandler {
	
	private final RateLimiter rateLimiter;
	private final Route route;
	
	private final List<APIRequest> queue;
	
	private boolean waiting;
	
	/**
	 * 
	 */
	public RequestHandler(Route route) {
		this.route = route;
		this.rateLimiter = new RateLimiter(route);
		queue = new ArrayList<>();
		waiting = false;
	}
	

}
