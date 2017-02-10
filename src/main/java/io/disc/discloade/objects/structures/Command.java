package io.disc.discloader.objects.structures;

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
	 * @param message
	 */
	public void execute(Message message) {
		return;
	}

	/**
	 * @return the unlocalizedName
	 */
	public String getUnlocalizedName() {
		return unlocalizedName;
	}

}
