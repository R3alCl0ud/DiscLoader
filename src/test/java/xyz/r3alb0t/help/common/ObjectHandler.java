package xyz.r3alb0t.help.common;

import io.discloader.discloader.objects.loader.DiscRegistry;
import io.discloader.discloader.objects.structures.Command;
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
		DiscRegistry.registerCommand(HelpCommand, HelpCommand.getUnlocalizedName());
	}

}
