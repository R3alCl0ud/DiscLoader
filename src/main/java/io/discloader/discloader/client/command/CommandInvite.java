package io.discloader.discloader.client.command;

import io.discloader.discloader.common.event.message.MessageCreateEvent;

public class CommandInvite extends Command {

	public CommandInvite() {
		super();
		this.setUnlocalizedName("invite").setUsage("invite").setDescription("returns the OAuth2 invite link");
	}

	public void execute(MessageCreateEvent e, String[] args) {
		// e.getChannel().startTyping().join();
		e.loader.user.getOAuth2Application().thenAcceptAsync(App -> {
			e.getChannel().sendMessage(App.getOAuthURL());
		});
	}
}
