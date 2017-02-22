package com.example.common;

import com.example.common.commands.ExampleCommand;

import io.discloader.discloader.client.command.Command;
import io.discloader.discloader.common.registry.CommandRegistry;

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
		HelpCommand = new ExampleCommand();
	}
	
	public static void registerCommands() {
		CommandRegistry.registerCommand(HelpCommand, HelpCommand.getUnlocalizedName());
	}

}
