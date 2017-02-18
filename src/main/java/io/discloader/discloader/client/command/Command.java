package io.discloader.discloader.client.command;

import io.discloader.discloader.client.renderer.util.IIcon;
import io.discloader.discloader.common.events.MessageCreateEvent;
import io.discloader.discloader.common.registry.CommandRegistry;
import io.discloader.discloader.util.NumericStringMap;

/**
 * @author Perry Berman
 *
 */
public class Command {

	private String unlocalizedName;
	
	private String textureName;
	
	private int id;
	
	protected IIcon icon;

	protected static NumericStringMap<Command> commands = CommandRegistry.commands;
	
	public Command() {
		
	}

	public static void registerCommands() {
		commands.addObject(0, "help", new CommandHelp().setUnlocalizedName("help"));
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
	 * 
	 * @param textureName MODID:icons.commands.unlocalizedName
	 */
	public void setTextureName(String textureName) {
		this.textureName = textureName;
	}
	
	public IIcon getIcon() {
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
	public void setId(int id) {
		this.id = id;
	}

}
