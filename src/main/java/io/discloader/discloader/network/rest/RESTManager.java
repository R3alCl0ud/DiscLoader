package io.discloader.discloader.network.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.google.gson.Gson;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.network.util.Methods;

public class RESTManager {

	public Gson gson;
	public HashMap<String, RESTQueue> queues;
	public DiscLoader loader;
	private final Map<String, Route<?>> routes;

	private boolean globally;

	public RESTManager(DiscLoader loader) {
		this.loader = loader;
		gson = new Gson();
		queues = new HashMap<>();
		routes = new HashMap<>();
		setGlobally(false);
	}

	/**
	 * @param <T>
	 *            The type to parse request response data as
	 * @param method
	 * @param url
	 * @param options
	 * @param cls
	 *            The {@link Class} of Type T
	 * @return A {@link CompletableFuture} Object that completes with an Object of
	 *         Type if applicable.
	 */
	@SuppressWarnings("unchecked")
	public <T> CompletableFuture<T> request(Methods method, String url, RESTOptions options, Class<T> cls) {
		if (options == null)
			options = new RESTOptions();
		Request<T> apiRequest = new Request<T>(options.getPayload());
		if (!routes.containsKey(url))
			routes.put(url, new Route<T>(this, url, method, options.isAuth(), cls));
		return ((Route<T>) routes.get(url)).push(apiRequest);
	}

	public void handleQueue(String route) {
		this.queues.get(route).handle();
	}

	public CompletableFuture<String> makeRequest(String url, int method, boolean auth) {
		return this.makeRequest(url, method, auth, null);
	}

	public CompletableFuture<String> makeRequest(String url, int method, boolean auth, Object data) {
		APIRequest request = new APIRequest(url, method, auth, data);
		CompletableFuture<String> future = new CompletableFuture<>();
		if (!queues.containsKey(url)) {
			queues.put(url, new RESTQueue(this, url));
		}

		request.setFuture(future);
		queues.get(url).addToQueue(request);
		handleQueue(url);
		return future;
	}

	/**
	 * @return the globally
	 */
	public boolean isGlobally() {
		return this.globally;
	}

	/**
	 * @param globally
	 *            the globally to set
	 */
	public void setGlobally(boolean globally) {
		this.globally = globally;
	}

}
