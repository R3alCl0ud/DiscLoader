package io.discloader.discloader.client.command;

import java.util.Locale;
import java.util.regex.Matcher;

import io.discloader.discloader.common.event.message.MessageCreateEvent;
import io.discloader.discloader.common.language.LanguageRegistry;
import io.discloader.discloader.common.logger.DLLogger;
import io.discloader.discloader.common.registry.CommandRegistry;
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
			if (!handleCommands || e.loader.user == null || message.getAuthor() == null || message.getAuthor().isBot() || ((!e.loader.user.isBot() && selfBot) && message.getAuthor().getID() != e.loader.user.getID()) || message.getContent().length() < prefix.length()) {
				return;
			}
			String[] Args = e.args;
			String label = Args[0];
			String rest = "";
			if (label.length() < message.getContent().length())
				rest = message.getContent().substring(label.length() + 1);
			int argc = Args.length > 1 ? Args.length - 1 : 0;

			if (label.length() < prefix.length() || !label.substring(0, prefix.length()).equals(prefix)) {
				return;
			}
			label = label.substring(prefix.length());
			Command command = getCommand(label, message);
			if (command != null) {
				if (message.getChannel() instanceof IGuildTextChannel && !command.shouldExecute(message.getMember(), (IGuildTextChannel) message.getChannel()))
					return;
				String[] args = new String[argc];
				Matcher argM = command.getArgsPattern().matcher(rest);
				int n = 0;
				while (argM.find() && n < args.length) {
					for (int i = 0; i < argM.groupCount() && i < args.length; i++) {
						try {
							args[n] = argM.group(i);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
					n++;
				}
				Thread handler = new Thread(command.getUnlocalizedName() + " - Message Handler") {
					@Override
					public void run() {
						try {
							command.execute(e, args);
						} catch (Exception ex) {
							DLLogger.getLogger(CommandHandler.class).severe("Error Occurred While attempting to execute the command \"" + command.getUnlocalizedName() + "\".");
							ex.printStackTrace();
						}
					}
				};
				handler.setPriority(Thread.MAX_PRIORITY - 2);
				handler.start();
				return;
			}
		} catch (Exception ex) {
			System.err.println("Error occurred whilst attempting to handle commands.");
			ex.printStackTrace();
		}
	}

	/**
	 * @param label
	 *            The label of the command
	 * @param message
	 *            The message the label is from
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
			if ((localized != null && localized.equals(label)) || command.getUnlocalizedName().equals(label) || command.getAliases().contains(label)) {
				return command;
			}
		}
		return null;
	}

	public static String getGuildRegion(IGuild guild) {
		return guild.getVoiceRegion().id;
	}

}
