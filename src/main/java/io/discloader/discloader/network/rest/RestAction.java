package io.discloader.discloader.network.rest;

import java.util.concurrent.CompletableFuture;

/**
 * @author Perry Berman
 *
 * @param <T> Completion type
 * @param <U> Parse type
 */
public class RestAction<T, U> {
	
	private final CompletableFuture<T> future;
	
	/**
	 * 
	 */
	public RestAction() {
		future = new CompletableFuture<>();
	}
	
	/**
	 * @return the future
	 */
	public CompletableFuture<T> getFuture() {
		return this.future;
	}
}
