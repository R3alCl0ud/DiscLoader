package io.disc.DiscLoader.rest;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;

import com.google.gson.Gson;

import io.disc.DiscLoader.DiscLoader;
import io.disc.DiscLoader.objects.gateway.MessageJSON;
import io.disc.DiscLoader.objects.structures.Channel;
import io.disc.DiscLoader.objects.structures.Message;
import io.disc.DiscLoader.objects.structures.TextChannel;
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

		request.setFuture(future);
		this.queues.get(url).addToQueue(request);
		this.handleQueue(url);
		return future;
	}

	public CompletableFuture<String> makeRequest(String url, int method, boolean auth) {
		return this.makeRequest(url, method, auth, null);
	}

	public CompletableFuture<Message> sendMessage(TextChannel channel, String content) {
		if (content.length() < 1)
			return null;
		CompletableFuture<Message> msgSent = new CompletableFuture<Message>();
		this.makeRequest(constants.Endpoints.messages(channel.id), constants.Methods.POST, true,
				new JSONObject().put("content", content).toString()).thenAcceptAsync(action -> {
					msgSent.complete(new Message(channel, this.gson.fromJson(action, MessageJSON.class)));
				});
		return msgSent;
	}

	public CompletableFuture<Message> editMessage(TextChannel channel, Message message, String content) {
		if (content.length() < 1)
			return null;
		System.out.println(constants.Endpoints.message(channel.id, message.id));
		CompletableFuture<Message> future = new CompletableFuture<Message>();
		this.makeRequest(constants.Endpoints.message(channel.id, message.id), constants.Methods.PATCH, true,
				new JSONObject().put("content", content).toString()).thenAcceptAsync(action -> {
					future.complete(new Message(channel, this.gson.fromJson(action, MessageJSON.class)));
				});
		return future;
	}

	public CompletableFuture<Message> deleteMessage(TextChannel channel, Message message) {
		System.out.println(constants.Endpoints.message(channel.id, message.id));
		CompletableFuture<Message> future = new CompletableFuture<Message>();
		this.makeRequest(constants.Endpoints.message(channel.id, message.id), constants.Methods.DELETE, true)
				.thenAcceptAsync(action -> {
					future.complete(message);
				});
		return future;
	}
	
}
