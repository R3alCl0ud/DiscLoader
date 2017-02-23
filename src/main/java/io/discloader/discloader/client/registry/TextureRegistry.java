package io.discloader.discloader.client.registry;

import java.util.HashMap;

import io.discloader.discloader.client.command.Command;
import io.discloader.discloader.client.render.ResourceHandler;
import io.discloader.discloader.client.render.texture.icon.CommandIcon;
import io.discloader.discloader.client.render.texture.icon.GuildIcon;
import io.discloader.discloader.client.render.texture.icon.UserIcon;

/**
 * @author Perry Berman
 *
 */
public class TextureRegistry {

	public static final ResourceHandler resourceHandler = new ResourceHandler();
	
	private static HashMap<Integer, CommandIcon> commandIcons = new HashMap<Integer, CommandIcon>();
	private static HashMap<String, UserIcon> userIcons = new HashMap<String, UserIcon>();
	private static HashMap<String, GuildIcon> guildIcons= new HashMap<String, GuildIcon>();

	public static CommandIcon registerCommandIcon(Command command) {
		CommandIcon icon = new CommandIcon(command);
		commandIcons.put(command.getId(), icon);
		return icon;
	}

	public static void registerUserIcon(UserIcon icon) {
		userIcons.put(icon.getIconName(), icon);
	}

	public static void registerGuildIcon(GuildIcon icon) {
		guildIcons.put(icon.getIconName(), icon);
	}

	/**
	 * @return the icons
	 */
	public static HashMap<Integer, CommandIcon> getCommandIcons() {
		return commandIcons;
	}

	/**
	 * @return the userIcons
	 */
	public static HashMap<String, UserIcon> getUserIcons() {
		return userIcons;
	}

	/**
	 * @return the guildIcons
	 */
	public static HashMap<String, GuildIcon> getGuildIcons() {
		return guildIcons;
	}
}
