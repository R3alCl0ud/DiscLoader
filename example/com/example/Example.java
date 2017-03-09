package com.example;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.EventListenerAdapter;
import io.discloader.discloader.common.event.MessageCreateEvent;

public class Example extends EventListenerAdapter {

	public static void main(String... args) {
		DiscLoader client = new DiscLoader();
		DiscLoader.addEventHandler(new Example());
		client.login("TOKEN");
	}

	public void MessageCreate(MessageCreateEvent e) {
		if (!e.message.content.startsWith("/"))
			return;

		if (e.message.content.equals("/hello")) {
			// message.author.toString() returns their mention string
			e.message.channel.sendMessage(String.format("Hello %s", e.message.author));
		}
	}

}
