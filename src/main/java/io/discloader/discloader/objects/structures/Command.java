package io.discloader.discloader.objects.structures;

import io.discloader.discloader.events.MessageCreateEvent;

/**
 * @author Perry Berman
 *
 */
public class Command {

	private String unlocalizedName;

	public Command() {

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
