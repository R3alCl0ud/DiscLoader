package io.discloader.discloader.client.registry;

import java.util.HashMap;

import io.discloader.discloader.client.command.Command;
import io.discloader.discloader.client.render.ResourceHandler;
import io.discloader.discloader.client.render.texture.AbstractTexture;
import io.discloader.discloader.client.render.texture.icon.CommandIcon;
import io.discloader.discloader.client.render.texture.icon.GuildIcon;
import io.discloader.discloader.client.render.texture.icon.UserIcon;

/**
 * @author Perry Berman
 *
 */
public class TextureRegistry {

	public final ResourceHandler resourceHandler;
	
	private HashMap<Integer, AbstractTexture> commandIcons;
	private HashMap<String, UserIcon> userIcons;
	private HashMap<String, GuildIcon> guildIcons;

	public TextureRegistry() {
		this.resourceHandler = new ResourceHandler();
		this.commandIcons = new HashMap<Integer, AbstractTexture>();
		this.userIcons = new HashMap<String, UserIcon>();
		this.guildIcons = new HashMap<String, GuildIcon>();
	}

	public void registerCommandIcon(Command command) {
		
		this.commandIcons.put(command.getId(), new CommandIcon(command));
	}

	public void registerUserIcon(UserIcon icon) {
		this.userIcons.put(icon.getIconName(), icon);
	}

	public void registerGuildIcon(GuildIcon icon) {
		this.guildIcons.put(icon.getIconName(), icon);
	}

	/**
	 * @return the icons
	 */
	public HashMap<Integer, AbstractTexture> getCommandIcons() {
		return commandIcons;
	}

	/**
	 * @return the userIcons
	 */
	public HashMap<String, UserIcon> getUserIcons() {
		return userIcons;
	}

	/**
	 * @return the guildIcons
	 */
	public HashMap<String, GuildIcon> getGuildIcons() {
		return guildIcons;
	}
}
