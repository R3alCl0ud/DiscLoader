package io.discloader.discloader.common.registry;

import io.discloader.discloader.client.command.Command;
import io.discloader.discloader.client.logger.ProgressLogger;
import io.discloader.discloader.common.discovery.ModContainer;
import io.discloader.discloader.util.DLNameSpacedMap;

/**
 * @author Perry Berman
 *
 */
public class CommandRegistry {
	
	private static int minID = 128;
	private static int maxID = 2048;
	
	public static final DLNameSpacedMap<Command> commands = new DLNameSpacedMap<Command>();
	
	public static String addPrefix(String name) {
		int index = name.lastIndexOf(":");
		String oldPrefix = index == -1 ? "" : name.substring(0, index), prefix;
		
		ModContainer modContainer = ModRegistry.activeMod;
		
		if (modContainer != null) {
			prefix = modContainer.modInfo.modid();
		} else {
			prefix = "discloader";
		}
		
		if (!oldPrefix.equals(prefix)) {
			name = prefix + ":" + name;
		}
		
		return name;
	}
	
	/**
	 * Registers a command. <u>This method is to only be internally executed by DiscLoader itself</u><br>
	 * Mod makers should never call this method, it is considered bad practice, and will stop the client from starting up in the future
	 * 
	 * @param command The command to register
	 * @param name The namespaced name of the command
	 * @param idHint what the command's id should be if no other command is already registered with the id
	 */
	private static void registerCommand(Command command, String name, int idHint) {
		int id = idHint;
		if (id == -1) {
			id = (int) (Math.round(Math.random() * (maxID - minID)) + minID);
		}
		
		while (commands.containsID(id)) {
			id = (int) (Math.round(Math.random() * (maxID - minID)) + minID);
		}
		command.setId(id);
		commands.set(id, name, command.getUnlocalizedName(), command);
	}
	
	/**
	 * Registers a command.
	 * 
	 * @param command The command to be registered
	 * @param name A unique name for the command. It is <u>strongly</u> recommended that this should be the command's unlocalized name
	 */
	public static void registerCommand(Command command, String name) {
		if (ProgressLogger.phaseNumber != 2) {
			// System.err.println(String.format("", ));
		}
		registerCommand(command, addPrefix(name), -1);
	}
	
}
