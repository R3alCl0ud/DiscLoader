/**
 * 
 */
package com.example.common.commands;

import io.discloader.discloader.client.command.Command;
import io.discloader.discloader.common.event.MessageCreateEvent;
import io.discloader.discloader.entity.message.Message;

/**
 * @author Perry Berman
 *
 */
public class ExampleCommand extends Command {
	public ExampleCommand() {
		super();
		this.setUnlocalizedName("example");
		this.setTextureName("examplemod:example");
	}

	public void execute(MessageCreateEvent e) {
		Message message = e.getMessage();
		message.channel.sendMessage(String.format("Hello %s\nThis is an example command", message.author.toString()));
	}

}
