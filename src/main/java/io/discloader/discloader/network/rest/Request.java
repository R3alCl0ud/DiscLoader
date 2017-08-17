package io.discloader.discloader.network.rest;

import java.util.concurrent.CompletableFuture;

/**
 * @author Perry Berman
 * @param <T> the type the request completes with.
 */
public class Request<T> {
	public static final String DISCORD_API_PREFIX = "https://discordapp.com/api/v6";
	
	private CompletableFuture<T> future;
	
	private Object data;
	
	public Request(Object data) {
		future = new CompletableFuture<>();
		this.data = data;
	}
	
	public Object getData() {
		return data;
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
	
}
