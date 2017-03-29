package io.discloader.discloader.network.rest;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.network.util.Methods;

/**
 * @author Perry Berman
 *
 * @param <T>
 */
public class Request<T> {
	
	public static final String DISCORD_API_PREFIX = "https://discordapp.com/api/v6";
	
	private CompletableFuture<T> future;
	
	private String endpoint;
	private Methods method;
	private Object data;
	
	public Request(String endpoint, Methods method, Object data) {
		future = new CompletableFuture<>();
		this.endpoint = endpoint;
		this.method = method;
		this.data = data;
	}
	
	/**
	 * @return the endpoint
	 */
	public String getEndpoint() {
		return this.endpoint;
	}
	
	/**
	 * @return the method
	 */
	public Methods getMethod() {
		return this.method;
	}
	
	public Object getData() {
		return data;
	}
	
	public void failed(String response) {
		
	}
	
	public void complete(String response) {
		
	}
	
}
