package main.java.com.forgecord.structures;

import main.java.com.forgecord.client.Client;

public class Channel {
	
	public String id;
	public Client client;
	
	public Channel(Client client) {
		this.client = client;
	}
	
	public void sendMessage(String message) {
//		this.client.rest;
	}
}
