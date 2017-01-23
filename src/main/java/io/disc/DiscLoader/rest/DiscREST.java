package io.disc.DiscLoader.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.disc.DiscLoader.DiscLoader;

public class DiscREST {
	public HashMap<String, ArrayList<APIRequest>> queues;
	public DiscLoader loader;

	public DiscREST(DiscLoader loader) {
		this.loader = loader;

		this.queues = new HashMap<String, ArrayList<APIRequest>>();
	}

	public void handleQueue(String route) {

	}

	public CompletableFuture<?> makeRequest(String url, int method, boolean auth, Object data) {
		APIRequest request = new APIRequest(url, method, auth, data);
		CompletableFuture<?> future = new CompletableFuture<String>();
		this.queues.get(url).add(request);
		return request.setFuture(future);
	}

	public CompletableFuture<?> makeRequest(String url, int method, boolean auth) {
		return this.makeRequest(url, method, auth, null);
	}
}
