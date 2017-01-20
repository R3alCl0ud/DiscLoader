package main.java.com.forgecord.structures;

import java.util.concurrent.CompletableFuture;

import main.java.com.forgecord.client.Client;

public class Channel {

	public String id;
	public Client client;

	/**
	 * Channel base class, All channel types extend this class
	 * 
	 * @param client
	 *            The client that instantiated the channel
	 */
	public Channel(Client client) {
		this.client = client;
	}

	/**
	 * Sends a message to the channel
	 * @param content The message's content
	 * @return {@link CompletableFuture}
	 */
	public CompletableFuture<String> sendMessage(String content) {
		return null;
	}
}
