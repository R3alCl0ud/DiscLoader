package io.discloader.discloader.client.command;

import java.io.File;

import io.discloader.discloader.client.registry.TextureRegistry;
import io.discloader.discloader.client.render.texture.icon.CommandIcon;
import io.discloader.discloader.common.discovery.ModContainer;
import io.discloader.discloader.common.events.MessageCreateEvent;
import io.discloader.discloader.common.registry.CommandRegistry;
import io.discloader.discloader.common.registry.ModRegistry;
import io.discloader.discloader.entity.RichEmbed;

/**
 * 
 * 
 * @author Perry Berman
 */
public class CommandHelp extends Command {

	public CommandHelp() {
		super();
		this.setTextureName("discloader:icon.commands.help");
	}

	public void execute(MessageCreateEvent e) {
		RichEmbed embed = new RichEmbed("Help");

		if (e.message.content.split(" ").length > 1) {
			String mod = e.message.content.split(" ")[1];
			if (ModRegistry.mods.containsKey(mod)) {
				ModContainer mc = ModRegistry.mods.get(mod);
				embed.setThumbnail("assets/discloader/texture/icons/missing-icon.png");

			}
			return;
		}


		embed.setColor(0x08a2ff)
				.setThumbnail(new File(
						ClassLoader.getSystemResource("assets/discloader/texture/icons/commands/help.png").getFile()))
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
		embed.addField("Mods", mods);
		embed.addField("Default Commands", dCommands);
		e.message.channel.sendEmbed(embed);
	}

}
