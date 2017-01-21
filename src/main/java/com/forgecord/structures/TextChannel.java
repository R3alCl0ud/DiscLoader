package main.java.com.forgecord.structures;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;

import main.java.com.forgecord.client.Client;
import main.java.com.forgecord.util.constants;

public class TextChannel extends Channel {

	/**
	 * A {@link HashMap} containing the messages sent to this channel
	 */
	public HashMap<String, Message> messages = null;
	
	public TextChannel(Client client, JSONObject data) {
		super(client, data);
		this.type = "text";
	}
	
	/**
	 * @return the sent {@link Message message}
	 */
	public CompletableFuture<Message> sendMessage(String content, Optional<JSONObject> Opt) {
		JSONObject payload = new JSONObject().put("content", content);
		if (Opt.isPresent()) {
			JSONObject options = Opt.get();
			if (options.has("tts")) payload.put("tts", options.getBoolean("tts"));
			if (options.has("embed")) payload.put("embed", payload.get("embed"));
		}
		CompletableFuture<Message> message = new CompletableFuture<Message>();
		this.client.rest.createRequest(constants.Endpoints.messages(this.id), constants.Methods.POST, true, payload).thenAcceptAsync(msg -> {
			message.complete(new Message(this.client, new JSONObject(msg)));
		});
		return message;
	}
	
}
