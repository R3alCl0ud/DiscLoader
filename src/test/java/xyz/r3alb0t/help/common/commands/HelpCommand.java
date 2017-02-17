/**
 * 
 */
package xyz.r3alb0t.help.common.commands;

import io.discloader.discloader.client.command.Command;
import io.discloader.discloader.common.events.MessageCreateEvent;
import io.discloader.discloader.common.structures.Message;

/**
 * @author Perry Berman
 *
 */
public class HelpCommand  extends Command {
	public HelpCommand() {
		super();
		this.setUnlocalizedName("helpBot");
	}
	
	public void execute(MessageCreateEvent e) {
		Message message = e.message;
		message.channel.sendMessage("Showing help for: " + message.author.toString());
	}

}
