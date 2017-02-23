package io.discloader.discloader.common.registry;

import java.util.HashMap;

import io.discloader.discloader.client.command.Command;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.discovery.ModContainer;
import io.discloader.discloader.common.event.MessageCreateEvent;

/**
 * @author Perry Berman
 *
 */
@Deprecated
public class DiscRegistry {

	public final DiscLoader loader;

	public static String prefix;
	
	public static final HashMap<String, Command> commands = new HashMap<String, Command>();
	
	private static ModContainer currentActiveMod;

	public DiscRegistry(DiscLoader loader) {
		this.loader = loader;
	}

	public static void registerCommand(Command command, String uniqueID) {
		commands.put(currentActiveMod.modInfo.modid() + ":" + uniqueID, command);
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
