/**
 * 
 */
package com.example.common.commands;

import io.discloader.discloader.client.command.Command;
import io.discloader.discloader.common.event.message.MessageCreateEvent;
import io.discloader.discloader.entity.message.IMessage;

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
	
	@Override
	public void execute(MessageCreateEvent e) {
		IMessage message = e.getMessage();
		message.getChannel().sendMessage(String.format("Hello %s\nThis is an example command", message.getAuthor().toString()));
	}
	
}
