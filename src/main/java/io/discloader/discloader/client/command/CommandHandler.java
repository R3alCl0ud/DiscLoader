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
        if (!handleCommands || e.message.author.bot || e.message.content.length() < prefix.length()) {
            return;
        }
        Message message = e.message;
        String label = message.content.split(" ")[0];
        if (label.length() < prefix.length() || !label.substring(0, prefix.length()).equals(prefix)) {
            return;
        }
        
        label = label.substring(prefix.length());

        Command command = getCommand(label, message);
        if (command != null) {
        	System.out.println(command.getUnlocalizedName());
            command.execute(e);
        }
    }

    /**
     * @param label The label of the command
     * @param message The message the label is from
     * @return A Command if a command was found, {@code null} if no command was found
     */
    public static Command getCommand(String label, Message message) {
        String region = message.guild != null ? getGuildRegion(message.guild) : "us-central";
        Locale locale = Locale.US;
        if (region.startsWith("us")) {
            locale = Locale.US;
        } else if (region.startsWith("uk")) {
            locale = Locale.UK;
        }
            
        
        for (Command command : CommandRegistry.commands.entries()) {
            String localized = LanguageRegistry.getLocalized(locale, "command", command.getUnlocalizedName(), "name");
            if ((localized != null && localized.equals(label)) || command.getUnlocalizedName().equals(label)) {
                return command;
            }
        }
        return null;
    }
    
    public static String getGuildRegion(Guild guild) {
        return guild.voiceRegion.id;
    }

}
