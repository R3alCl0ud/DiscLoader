package io.disc.DiscLoader;

import java.util.concurrent.CompletableFuture;

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

	public CompletableFuture<?> login(String token) {
		this.token = token;
		
		return this.rest.makeRequest(constants.Endpoints.gateway, constants.Methods.GET, true);
	}
}
