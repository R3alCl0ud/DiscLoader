package io.disc.DiscLoader;

import java.util.concurrent.CompletableFuture;

import io.disc.DiscLoader.socket.DiscSocket;

/**
 * @author Perry Berman
 *
 */
public class DiscLoader {
	
	public DiscSocket discSocket;
	public String token;
	
	public DiscLoader() {
		this.discSocket = new DiscSocket(this);
	}
	
	public CompletableFuture<String> login(String token) {
		this.token = token;
		CompletableFuture<String> future = new CompletableFuture<String>();
		return future;
	}
}
