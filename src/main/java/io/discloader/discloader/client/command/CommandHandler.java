package io.discloader.discloader.client.command;

import io.discloader.discloader.common.event.MessageCreateEvent;
import io.discloader.discloader.common.registry.CommandRegistry;
import io.discloader.discloader.entity.Guild;
import io.discloader.discloader.entity.Message;

/**
 * @author Perry Berman
 * @since 0.0.1
 */
public class CommandHandler {

	public static String prefix = "/";
	
	public static boolean handleCommands = false;

	public static void handleMessageCreate(MessageCreateEvent e) {
		if (!handleCommands || e.message.author.bot) {
			return;
		}
		
		Message message = e.message;
		String label = message.content.split(" ")[0].substring(prefix.length());

		String region = getGuildRegion(message.guild);
		if (region.startsWith("us")) {
			Command command = CommandRegistry.commands.get(label);
			if (command != null) {
				command.execute(e);
			}
		}
	}

	public static String getGuildRegion(Guild guild) {
		return "us-central";
	}

}
