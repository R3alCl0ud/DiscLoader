package io.discloader.discloader.test;

import java.util.concurrent.ExecutionException;

import io.discloader.discloader.client.command.Command;
import io.discloader.discloader.common.event.message.MessageCreateEvent;
import io.discloader.discloader.common.exceptions.UnauthorizedException;

public class CommandCreateGuild extends Command {

	public CommandCreateGuild() {
		setUnlocalizedName("createguild");
	}

	public void execute(MessageCreateEvent e) {
		if (e.getMessage().getGuild() != null || Main.appOwner == null || e.getMessage().getAuthor().getID() != Main.appOwner.getID()) {
			return;
		}
		try {
			if (Main.createGuild()) {
				Main.appOwner.sendMessage("Guild Successfully Created");
			}
		} catch (UnauthorizedException | InterruptedException | ExecutionException ex) {
			Main.appOwner.sendMessage("Error occurred: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
}
