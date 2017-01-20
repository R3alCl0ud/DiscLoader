package main.java.com.forgecord.client.rest;

import org.json.JSONObject;

import main.java.com.forgecord.client.Client;
import main.java.com.forgecord.client.rest.RequestHandlers.SequentialRequestHandler;

import java.util.concurrent.*;

public class RESTManager {

	public Client client;
	public RESTMethods methods;
	public JSONObject handlers;

	public RESTManager(Client client) {
		this.client = client;
		this.methods = new RESTMethods(this);
		this.handlers = new JSONObject();
	}

	public CompletableFuture<String> push(SequentialRequestHandler handler, APIRequest apiRequest) {
		CompletableFuture<String> future = new CompletableFuture<String>();
		apiRequest.setFuture(future);
		handler.push(apiRequest);
		return future;
	}

	public CompletableFuture<String> createRequest(String url, String method, boolean auth, JSONObject data) {
		APIRequest apiRequest = new APIRequest(this, method, url, auth, data);
		if (!this.handlers.has(apiRequest.route))
			this.handlers.put(apiRequest.route, new SequentialRequestHandler(this, apiRequest.route));
		SequentialRequestHandler handler = (SequentialRequestHandler) this.handlers.get(apiRequest.route);
		return this.push(handler, apiRequest);
	}

	public CompletableFuture<String> createRequest(String url, String method, boolean auth) {
		APIRequest apiRequest = new APIRequest(this, method, url, auth, new JSONObject());
		if (!this.handlers.has(apiRequest.route))
			this.handlers.put(apiRequest.route, new SequentialRequestHandler(this, apiRequest.route));
		SequentialRequestHandler handler = (SequentialRequestHandler) this.handlers.get(apiRequest.route);
		return this.push(handler, apiRequest);
	}

}
