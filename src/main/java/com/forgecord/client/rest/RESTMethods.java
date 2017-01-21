package main.java.com.forgecord.client.rest;

import java.util.concurrent.CompletableFuture;

import main.java.com.forgecord.client.Client;
import main.java.com.forgecord.util.constants;

public class RESTMethods {

	public Client client;
	public RESTManager rest;

	public RESTMethods(RESTManager restManager) {
		this.rest = restManager;
		this.client = restManager.client;
	}

	public void login(String token) {
		this.client.manager.connectToWebSocket(token);
	}

	public CompletableFuture<?> getGateway() {
		return this.rest.createRequest(constants.Endpoints.gateway, constants.Methods.GET, true);
	}

	public void logout() {

	}
}
