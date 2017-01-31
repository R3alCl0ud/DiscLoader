package io.disc.DiscLoader.rest;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;

import com.google.gson.Gson;

import io.disc.DiscLoader.DiscLoader;
import io.disc.DiscLoader.objects.gateway.MessageJSON;
import io.disc.DiscLoader.objects.structures.Channel;
import io.disc.DiscLoader.objects.structures.Message;
import io.disc.DiscLoader.util.constants;

public class DiscREST {
	public Gson gson;
	public HashMap<String, DiscRESTQueue> queues;
	public DiscLoader loader;

	public DiscREST(DiscLoader loader) {
		this.loader = loader;

		this.gson = new Gson();
		
		this.queues = new HashMap<String, DiscRESTQueue>();
	}

	public void handleQueue(String route) {
		this.queues.get(route).handle();
	}

	public CompletableFuture<String> makeRequest(String url, int method, boolean auth, Object data) {
		APIRequest request = new APIRequest(url, method, auth, data);
		CompletableFuture<String> future = new CompletableFuture<String>();
		if (!this.queues.containsKey(url)) {
			this.queues.put(url, new DiscRESTQueue(this));
		}

		this.queues.get(url).addToQueue(request);
		request.setFuture(future);
		this.handleQueue(url);
		return future;
	}

	public CompletableFuture<String> makeRequest(String url, int method, boolean auth) {
		return this.makeRequest(url, method, auth, null);
	}

	public CompletableFuture<Message> sendMessage(Channel channel, String content) {
		if (content.length() < 1) return null; 
		CompletableFuture<Message> msgSent = new CompletableFuture<Message>();
		this.makeRequest(constants.Endpoints.messages(channel.id), constants.Methods.POST, true,
				new JSONObject().put("content", content)).thenAcceptAsync(action -> {
					System.out.println(action);
					msgSent.complete(new Message(this.loader, channel,this.gson.fromJson(action, MessageJSON.class)));
				});
		return msgSent;
	}
}
