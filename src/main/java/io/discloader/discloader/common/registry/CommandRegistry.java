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

	public static void registerCommand(Command command, String name, int idHint) {
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

	public static void registerCommand(Command command, String name) {
		if (ProgressLogger.phaseNumber != 2) {
			System.out.println("HALTING.......\nERROR: Commands must be registered in the PreINIT Phase");
			System.exit(1);
		}
		registerCommand(command, addPrefix(name), -1);
	}

}
