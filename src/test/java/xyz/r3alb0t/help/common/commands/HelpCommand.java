/**
 * 
 */
package xyz.r3alb0t.help.common.commands;

import io.discloader.discloader.events.MessageCreateEvent;
import io.discloader.discloader.objects.structures.Command;
import io.discloader.discloader.objects.structures.Message;

/**
 * @author Perry Berman
 *
 */
public class HelpCommand  extends Command {
	public HelpCommand() {
		super();
		this.setUnlocalizedName("help");
	}
	
	public void execute(MessageCreateEvent e) {
		Message message = e.message;
		message.channel.sendMessage("Showing help for: " + message.author.toString());
	}

}
