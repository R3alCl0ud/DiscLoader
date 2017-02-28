package io.discloader.discloader.client.command;

import java.util.Locale;

import io.discloader.discloader.common.event.MessageCreateEvent;
import io.discloader.discloader.common.language.LanguageRegistry;
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
		
		String region = message.guild != null ? getGuildRegion(message.guild) : "us-central";
		if (region.startsWith("us")) {
			for (Command command : CommandRegistry.commands.entries()) {
				String localized = LanguageRegistry.getLocalized(Locale.US, "command", command.getUnlocalizedName(), "name");
				if ((localized != null && localized.equals(label)) || command.getUnlocalizedName().equals(label)) {
					command.execute(e);
					return;
				}
			}
		}
	}

	public static String getGuildRegion(Guild guild) {
		return guild.region.id;
	}

}
