package io.discloader.discloader.network.rest;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.network.util.Methods;

/**
 * @author Perry Berman
 * @param <T>
 *            the type the request completes with.
 */
public class Request<T> {
	public static final String DISCORD_API_PREFIX = "https://discordapp.com/api/v6";

	private CompletableFuture<T> future;

	private Object data;
	private RESTOptions options;

	private final Methods method;
	
	public Request(RESTOptions options, Methods method) {
		future = new CompletableFuture<>();
		this.options = options;
		this.method = method;
	}

	public Request(Object data, Methods method) {
		future = new CompletableFuture<>();
		this.data = data;
		this.method = method;
	}

	public Object getData() {
		return data != null ? data : options != null ? options.getPayload() : null;
	}

	public String getRoute(String url) {
		String route = url.split("[?]")[0];
		if (route.contains("/channels/") || route.contains("/guilds/")) {
			int startInd = route.contains("/channels/") ? route.indexOf("/channels/") : route.indexOf("/guilds/");
			String majorID = route.substring(startInd).split("/")[2];
			route = route.replaceAll("(\\d{8,})/g", ":id").replaceAll(":id", majorID);
		}
		return route;
	}

	public CompletableFuture<T> getFuture() {
		return future;
	}

	public RESTOptions getOptions() {
		return options;
	}

    public Methods getMethod() {
        return method;
    }

}
