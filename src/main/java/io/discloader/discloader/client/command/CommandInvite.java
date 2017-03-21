package io.discloader.discloader.client.command;

import io.discloader.discloader.common.event.MessageCreateEvent;

public class CommandInvite extends Command {

	public CommandInvite() {
		super();
		this.setUnlocalizedName("invite").setUsage("invite").setDescription("returns the OAuth2 invite link");
	}

	public void execute(MessageCreateEvent e, String[] args) {
		try {
			e.getMessage().channel.startTyping().get();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		e.loader.user.getOAuth2Application().thenAcceptAsync(App -> {
			e.getMessage().channel.sendMessage(App.getOAuthURL());
		});
	}
}
