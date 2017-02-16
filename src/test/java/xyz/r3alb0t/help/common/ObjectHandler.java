package xyz.r3alb0t.help.common;

import io.discloader.discloader.client.command.Command;
import io.discloader.discloader.common.registry.CommandRegistry;
import xyz.r3alb0t.help.common.commands.HelpCommand;

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
		CommandRegistry.registerCommand(HelpCommand, HelpCommand.getUnlocalizedName());
	}

}
