package io.disc.discloader.objects.loader;

import java.util.HashMap;

import io.disc.discloader.DiscLoader;
import io.disc.discloader.events.MessageCreateEvent;
import io.disc.discloader.objects.structures.Command;

/**
 * @author Perry Berman
 *
 */
public class DiscRegistry {

	public final DiscLoader loader;

	public static String prefix;
	
	public static final HashMap<String, Command> commands = new HashMap<String, Command>();
	
	private static ModContainer currentActiveMod;

	public DiscRegistry(DiscLoader loader) {
		this.loader = loader;
	}

	public static void registerCommand(Command command, String uniqueID) {
		commands.put(currentActiveMod.ModInfo.modid() + ":" + uniqueID, command);
	}

	/**
	 * @return the currentActiveMod
	 */
	public static ModContainer getCurrentActiveMod() {
		return currentActiveMod;
	}

	/**
	 * @param currentActiveMod the currentActiveMod to set
	 */
	public static void setCurrentActiveMod(ModContainer currentActiveMod) {
		DiscRegistry.currentActiveMod = currentActiveMod;
	}

	public static void executeCommand(MessageCreateEvent e) {
		Command cmd = getCommand(e.message.content.split(" ")[0].substring(prefix.length()));
		if (cmd != null) {
			cmd.execute(e);
		}
	}
	
	public static Command getCommand(String label) {
		for (String key : commands.keySet()) {
			String modID = key.split(":")[0];
			String commandID = key.split(":")[1];
			if (commandID.equals(label)) {
				System.out.printf("%s\n", key);
				return commands.get(key);
			}
		}
		return null;
	}
	
}
