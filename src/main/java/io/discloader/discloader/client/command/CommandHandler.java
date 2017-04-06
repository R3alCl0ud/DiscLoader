package io.discloader.discloader.client.command;

import java.util.Locale;
import java.util.regex.Matcher;

import io.discloader.discloader.common.event.message.MessageCreateEvent;
import io.discloader.discloader.common.language.LanguageRegistry;
import io.discloader.discloader.common.registry.CommandRegistry;
import io.discloader.discloader.entity.channel.IGuildChannel;
import io.discloader.discloader.entity.channel.IGuildTextChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.message.IMessage;

/**
 * @author Perry Berman
 * @since 0.0.1
 */
public class CommandHandler {

	public static String prefix = "/";

	public static boolean handleCommands = false;

	public static boolean selfBot = true;

	public static void handleMessageCreate(MessageCreateEvent e) {
		try {
			IMessage message = e.getMessage();
			if (!handleCommands || message.getAuthor().isBot() || ((!e.loader.user.bot && selfBot) && !message.getAuthor().getID().equals(e.loader.user.getID())) || message.getContent().length() < prefix.length()) {
				return;
			}
			String[] Args = e.args;
			String label = Args[0];
			String rest = "";
			if (label.length() < message.getContent().length()) rest = message.getContent().substring(label.length() + 1);
			int argc = Args.length > 1 ? Args.length - 1 : 1;

			if (label.length() < prefix.length() || !label.substring(0, prefix.length()).equals(prefix)) {
				return;
			}
			try {
				label = label.substring(prefix.length());
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			Command command = getCommand(label, message);
			if (command != null) {
				if (message.getChannel() instanceof IGuildChannel && !command.shouldExecute(message.getMember(), (IGuildChannel) message.getChannel())) return;
				String[] args = new String[argc];
				Matcher argM = command.getArgsPattern().matcher(rest);
				if (argM.find()) {
					for (int i = 0; i < argM.groupCount(); i++) {
						try {
							args[i] = argM.group(i + 1);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
				command.execute(e, args);
				return;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * @param label The label of the command
	 * @param message The message the label is from
	 * @return A Command if a command was found, {@code null} if no command was
	 *         found
	 */
	public static Command getCommand(String label, IMessage message) {
		String region = message.getGuild() != null ? getGuildRegion(message.getGuild()) : "us-central";
		Locale locale = Locale.US;
		if (region.startsWith("us")) {
			locale = Locale.US;
		} else if (region.startsWith("london")) {
			locale = Locale.UK;
		}

		for (Command command : CommandRegistry.commands.entries()) {
			String localized = LanguageRegistry.getLocalized(locale, "command." + command.getUnlocalizedName() + ".name");
			if ((localized != null && localized.equals(label)) || command.getUnlocalizedName().equals(label)) {
				return command;
			}
		}
		return null;
	}

	public static String getGuildRegion(IGuild guild) {
		return guild.getVoiceRegion().id;
	}

}
