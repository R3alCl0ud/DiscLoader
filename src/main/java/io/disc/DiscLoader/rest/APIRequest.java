package io.disc.DiscLoader.rest;

import java.util.concurrent.CompletableFuture;

public class APIRequest {
	public String url;
	
	public int method;
	
	public boolean auth;
	
	public Object data;
	
	public CompletableFuture<?> future;
	
	public APIRequest(String url, int method, boolean auth, Object data) {
		this.url = url;
		this.method = method;
		this.auth = auth;
		this.data = data;
	}
	
	public CompletableFuture<?> setFuture(CompletableFuture<?> future) {
		this.future = future;
		return future;
	}
}
