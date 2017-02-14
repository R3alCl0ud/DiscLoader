/**
 * 
 */
package xyz.r3alb0t.help.common.commands;

import io.disc.discloader.events.MessageCreateEvent;
import io.disc.discloader.objects.structures.Command;
import io.disc.discloader.objects.structures.Message;

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
