package io.discloader.discloader.test;

import java.util.concurrent.ExecutionException;

import io.discloader.discloader.client.command.Command;
import io.discloader.discloader.common.event.message.MessageCreateEvent;
import io.discloader.discloader.common.exceptions.UnauthorizedException;

public class CommandDeleteGuild extends Command {

	public CommandDeleteGuild() {
		setUnlocalizedName("deleteguild");
	}

	public void execute(MessageCreateEvent e) {
		if (Main.guild == null || (e.getMessage().getGuild() != null && Main.guild.getID() != e.getMessage().getGuild().getID()) || Main.appOwner == null || e.getMessage().getAuthor().getID() != Main.appOwner.getID()) {
			return;
		}
		try {
			if (Main.deleteGuild()) {
				Main.appOwner.sendMessage("Guild Successfully Deleted");
			}
		} catch (UnauthorizedException | InterruptedException | ExecutionException ex) {
			Main.appOwner.sendMessage("Error occurred: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
}
