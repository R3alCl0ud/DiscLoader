package com.example;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.EventListenerAdapter;
import io.discloader.discloader.common.event.message.MessageCreateEvent;

public class Example extends EventListenerAdapter {

	public static void main(String... args) {
		DiscLoader client = new DiscLoader();
		client.addEventListener(new Example());
		client.login("TOKEN");
	}

	@Override
	public void Ready(ReadyEvent e) {
		System.out.println("We are ready to fly.");
	}
	
	@Override
	public void MessageCreate(MessageCreateEvent e) {
		if (e.getMessage().getAuthor().isBot() || !e.getMessage().getContent().startsWith("/"))
			return;
		String content = e.getMessage().getContent().subString(1);
		if (content.equals("hello")) {
			// <IMessage>.getAuthor().asMention() returns a string in mention format
			// <IMessage>.getAuthor().toString() return a string in username#discriminator
			// format
			e.getChannel().sendMessage(String.format("Hello %s", e.getMessage().getAuthor()));
		}
	}
}
