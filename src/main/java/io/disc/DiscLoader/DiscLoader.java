package io.disc.DiscLoader;

import java.util.concurrent.CompletableFuture;

import com.google.gson.Gson;

import io.disc.DiscLoader.events.DiscHandler;
import io.disc.DiscLoader.rest.DiscREST;
import io.disc.DiscLoader.socket.DiscSocket;
import io.disc.DiscLoader.util.constants;

/**
 * @author Perry Berman
 *
 */
public class DiscLoader {

	public DiscSocket discSocket;
	public String token;
	
	public DiscHandler handler;
	
	public DiscREST rest;

	public DiscLoader() {
		this.discSocket = new DiscSocket(this);
		this.handler = new DiscHandler(this);
		this.rest = new DiscREST(this);
		this.handler.loadEvents();
	}

	public CompletableFuture<String> login(String token) {
		this.token = token;
		CompletableFuture<String> future = (CompletableFuture<String>) this.rest.makeRequest(constants.Endpoints.gateway, constants.Methods.GET, true);
		future.thenAcceptAsync(text -> {
			System.out.println(text);
			Gson gson = new Gson();
			Gateway gateway = (Gateway) gson.fromJson(text, Gateway.class);
			this.discSocket.connectSocket(gateway.url + "?v=6&encoding=json");
		});
		return future;
	}
	
	public class Gateway {
		public String url;
	}
}
