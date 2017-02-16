package io.discloader.discloader.client.command;

import io.discloader.discloader.client.renderer.util.IIcon;
import io.discloader.discloader.common.events.MessageCreateEvent;

/**
 * @author Perry Berman
 *
 */
public class Command {

	private String unlocalizedName;
	
	protected IIcon commandIcon;

	public Command() {

	}

	public static void registerCommands() {
		
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

}
