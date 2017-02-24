package io.discloader.discloader.client.command;

import io.discloader.discloader.client.registry.TextureRegistry;
import io.discloader.discloader.client.render.util.IIcon;
import io.discloader.discloader.common.event.MessageCreateEvent;
import io.discloader.discloader.common.registry.CommandRegistry;
import io.discloader.discloader.util.NumericStringMap;

/**
 * @author Perry Berman
 *
 */
public class Command {

	private String unlocalizedName;
	
	private String textureName;
	
	private String description = "default description";
	
	private String usage = null;
	
	private int id;
	
	protected IIcon icon;

	protected static NumericStringMap<Command> commands = CommandRegistry.commands;
	
	public Command() {
		this.icon = null;
	}

	public static void registerCommands() {
		commands.addObject(0, "help", new CommandHelp().setUnlocalizedName("help"));
		commands.addObject(1, "mods", new CommandMods());
		commands.addObject(2, "invite", new CommandInvite());
	}
	
	
	/**
	 * @param unlocalizedName
	 * @return this
	 */
	public Command setUnlocalizedName(String unlocalizedName) {
		this.unlocalizedName = unlocalizedName;
		return this;
	}

	/**
	 * executes the command
	 * 
	 * @param e
	 */
	public void execute(MessageCreateEvent e) {
		return;
	}

	/**
	 * @return the unlocalizedName
	 */
	public String getUnlocalizedName() {
		return unlocalizedName;
	}

	/**
	 * @return the textureName
	 */
	public String getTextureName() {
		return textureName;
	}

	/**
	 * Sets the name of the texture for this command
	 * @param textureName MODID:icons.commands.unlocalizedName
	 */
	public Command setTextureName(String textureName) {
		this.textureName = textureName;
		return this;
	}
	
	public IIcon getIcon() {
		if (this.icon == null) {
			this.icon = TextureRegistry.registerCommandIcon(this);
		}
		return this.icon;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public Command setId(int id) {
		this.id = id;
		return this;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description the description to set
	 */
	public Command setDescription(String description) {
		this.description = description;
		return this;
	}

	/**
	 * @return the usage
	 */
	public String getUsage() {
		if (this.usage == null) {
			this.usage = this.getUnlocalizedName();
		}
		return this.usage;
	}

	/**
	 * @param usage the usage to set
	 */
	public Command setUsage(String usage) {
		this.usage = usage;
		return this;
	}

}
