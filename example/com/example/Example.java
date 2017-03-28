package com.example;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.EventListenerAdapter;
import io.discloader.discloader.common.event.message.MessageCreateEvent;

public class Example extends EventListenerAdapter {
	
	public static void main(String... args) {
		DiscLoader client = new DiscLoader();
		client.addEventHandler(new Example());
		client.login("TOKEN");
	}
	
	public void MessageCreate(MessageCreateEvent e) {
		if (!e.getMessage().content.startsWith("/"))
			return;
		
		if (e.getMessage().content.equals("/hello")) {
			// message.author.toString() returns their mention string
			e.getChannel().sendMessage(String.format("Hello %s", e.getMessage().author));
		}
	}
}
