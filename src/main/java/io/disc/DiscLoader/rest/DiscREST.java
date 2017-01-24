package io.disc.DiscLoader.rest;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.disc.DiscLoader.DiscLoader;

public class DiscREST {
	public HashMap<String, DiscRESTQueue> queues;
	public DiscLoader loader;

	public DiscREST(DiscLoader loader) {
		this.loader = loader;

		this.queues = new HashMap<String, DiscRESTQueue>();
	}

	public void handleQueue(String route) {

	}

	public CompletableFuture<String> makeRequest(String url, int method, boolean auth, Object data) {
		APIRequest request = new APIRequest(url, method, auth, data);
		CompletableFuture<String> future = new CompletableFuture<String>();
		if (!this.queues.containsKey(url)) {
			this.queues.put(url, new DiscRESTQueue(this));
		}
		
		this.queues.get(url).addToQueue(request);
		request.setFuture(future);
		this.handleQueue(url);
		return future; 
	}

	public CompletableFuture<String> makeRequest(String url, int method, boolean auth) {
		return this.makeRequest(url, method, auth, null);
	}
}
