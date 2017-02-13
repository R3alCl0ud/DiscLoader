package io.disc.discloader;

import io.disc.discloader.objects.annotations.Mod;
import io.disc.discloader.objects.annotations.Mod.Instance;
import io.disc.discloader.events.MessageCreateEvent;
import io.disc.discloader.objects.annotations.EventHandler;
import io.disc.discloader.objects.structures.Message;

/**
 * @author Perry Berman
 *
 */
@Mod(desc = HelpMod.DESC, modid = HelpMod.MODID, name = HelpMod.NAME, version = HelpMod.VERSION)
public class HelpMod {

	public static final String DESC = "Help Mod";
	public static final String MODID = "helpmod";
	public static final String NAME = "HelpMod";
	public static final String VERSION = "0.0.0";

	@Instance(MODID)
	public static HelpMod instance;
	
	public HelpMod() {
		
	}
	
	@EventHandler
	public void MessageCreate(MessageCreateEvent e) {
		Message message = e.message;
		if (!message.loader.user.bot && message.author.id != message.loader.user.id)
			return;

		if (message.content.equalsIgnoreCase("//test")) {
			message.channel.sendMessage("Hello there!\nGuild: " + (message.guild != null));
		} else if (message.content.startsWith("//nick") && message.guild != null
				&& message.author.id.equals("104063667351322624")) {
			String nick = message.content.split(" ")[1];
			message.guild.members.get(message.loader.user.id).setNick(nick);
		} else if (message.content.startsWith("//game") && message.author.id.equals("104063667351322624")) {
			String game = message.content.substring(7);
			message.loader.user.setGame(game);
			System.out.printf("Playing game set to: %s\n", game);
		}
	}

	@EventHandler
	public void raw(String raw) {
		System.out.println(raw);
	}

}
