package io.discloader.discloader.network.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.gson.Gson;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.network.util.Methods;

public class RESTManager {

	public Gson gson;
	public HashMap<String, RESTQueue> queues;
	public DiscLoader loader;
	private final Map<String, Route<?>> routes;

	/**
	 * Concurrently stores whether or not the global limit for REST requests has
	 * been reached.
	 */
	private static AtomicBoolean globally = new AtomicBoolean(false);

	public RESTManager(DiscLoader loader) {
		this.loader = loader;
		gson = new Gson();
		queues = new HashMap<>();
		routes = new HashMap<>();
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
		Request<T> apiRequest = new Request<T>(options, method);
		if (!routes.containsKey(url))
			routes.put(url, new Route<T>(this, url, method, options.isAuth(), cls));
		return ((Route<T>) routes.get(url)).push(apiRequest);
	}

	public void handleQueue(String route) {
		this.queues.get(route).handle();
	}

	public CompletableFuture<String> makeRequest(String url, Methods method, boolean auth) {
		return this.makeRequest(url, method, auth, null);
	}

	public CompletableFuture<String> makeRequest(String url, Methods method, boolean auth, Object data) {
		APIRequest request = new APIRequest(url, method.ordinal(), auth, data);
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
	 * Returns whether or not we have hit the global discord ratelimit.
	 * 
	 * @return Whether or not we have hit the global discord ratelimit.
	 */
	public boolean isGlobally() {
		return globally.get();
	}

	/**
	 * Sets whether or not we have hit the global discord ratelimit.
	 * 
	 * @param isGlobally
	 *            whether or not we have hit the global discord ratelimit.
	 */
	public void setGlobally(boolean isGlobally) {
		RESTManager.globally.set(isGlobally);
	}

}
