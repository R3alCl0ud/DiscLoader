package io.discloader.discloader.common.registry;

import java.util.HashMap;

import io.discloader.discloader.client.command.Command;
import io.discloader.discloader.common.discovery.ModContainer;
import io.discloader.discloader.util.NumericStringMap;

/**
 * @author Perry Berman
 *
 */
public class CommandRegistry {

	private static int minID = 128;
	private static int maxID = 2048;

	public static final NumericStringMap<Command> commands = new NumericStringMap<Command>();

	public static String addPrefix(String name) {
		int index = name.lastIndexOf(":");
		String oldPrefix = index == -1 ? "" : name.substring(0, index), prefix;

		ModContainer modContainer = ModRegistry.activeMod;

		if (modContainer != null) {
			prefix = modContainer.ModInfo.modid();
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
		if (idHint == -1) {
			id = (int) (Math.round(Math.random() * (maxID - minID)) + minID);
		}

		while (commands.containsID(idHint)) {
			id = (int) (Math.round(Math.random() * (maxID - minID)) + minID);
		}
		commands.set(id, name, command.getUnlocalizedName(), command);
	}

	public static void registerCommand(Command command, String name) {
		registerCommand(command, addPrefix(name), -1);
	}

}
