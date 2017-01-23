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

	}

	public void handleQueue(String route) {

	}

	public CompletableFuture<?> makeRequest(String url, String method, boolean auth, Object data) {
		return null;
	}

	public CompletableFuture<?> makeRequest(String url, String method, boolean auth) {
		return this.makeRequest(url, method, auth, null);
	}
}
