package io.disc.discloader.common;

import io.disc.discloader.objects.loader.DiscRegistry;
import io.disc.discloader.objects.structures.Command;
import io.disc.discloader.common.commands.HelpCommand;

/**
 * @author Perry Berman
 *
 */
public class ObjectHandler {

	public static Command HelpCommand;
	
	public static void register() {
		initCommands();
		registerCommands();
	}
	
	public static void initCommands() {
		HelpCommand = new HelpCommand();
	}
	
	public static void registerCommands() {
		DiscRegistry.registerCommand(HelpCommand, HelpCommand.getUnlocalizedName());
	}

}
