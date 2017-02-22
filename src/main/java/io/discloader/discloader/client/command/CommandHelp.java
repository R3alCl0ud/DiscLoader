package io.discloader.discloader.client.command;

import java.io.File;

import io.discloader.discloader.common.discovery.ModContainer;
import io.discloader.discloader.common.events.MessageCreateEvent;
import io.discloader.discloader.common.registry.CommandRegistry;
import io.discloader.discloader.common.registry.ModRegistry;
import io.discloader.discloader.common.structures.RichEmbed;

/**
 * 
 * 
 * @author Perry Berman
 */
public class CommandHelp extends Command {

	public CommandHelp() {
		super();
		this.setTextureName("discloader:icons.commands.help");
	}
	
	public void execute(MessageCreateEvent e) {
		RichEmbed embed = new RichEmbed("Help & Bot Info");
		
		if (e.message.content.split(" ").length > 1) {
			
			
			
			return;
		}
		
		//
		embed.setColor(0x08a2ff).setThumbnail(new File(ClassLoader.getSystemResource("assets/discloader/texture/icons/commands/help.png").getFile()))
				.setFooter("Generated using DiscLoader", e.loader.user.avatarURL)
				.setAuthor(e.loader.user.username, null, e.loader.user.avatarURL);
		String mods = "";
		for (ModContainer mod : ModRegistry.mods.values()) {
			mods += String.format("%s\n    Description: %s\n", mod.modInfo.name(), mod.modInfo.desc());
		}
		String dCommands = "";
		for (String id : CommandRegistry.commands.collectionCharIDs()) {
			if (id.indexOf(":") == -1) {
				Command command = CommandRegistry.commands.get(id);
				dCommands += String.format("%s\n", command.getUnlocalizedName());
			}
		}
		embed.addField("Stats", String.format("Guilds: %d, Users: %d", e.loader.guilds.size(), e.loader.users.size()), true);
		embed.addField("Mods", mods);
		embed.addField("Default Commands", dCommands);
		System.out.println(dCommands);
		e.message.channel.sendEmbed(embed);
	}

}
