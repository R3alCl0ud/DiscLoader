/**
 * 
 */
package io.discloader.discloader.client.command;

import io.discloader.discloader.common.discovery.ModContainer;
import io.discloader.discloader.common.event.MessageCreateEvent;
import io.discloader.discloader.common.registry.ModRegistry;
import io.discloader.discloader.entity.sendable.RichEmbed;
import io.discloader.discloader.util.Constants;

/**
 * @author Perry Berman
 *
 */
public class CommandMods extends Command {

	public CommandMods() {
		super();
		this.setUnlocalizedName("mods").setTextureName("discloader:mods").setDescription("mods").setUsage("words");
	}

	public void execute(MessageCreateEvent e) {
		RichEmbed embed = new RichEmbed();

		if (e.args.length == 2) {
			String mod = e.args[1];
			System.out.println(mod);
			ModContainer mc = ModRegistry.mods.get(mod);
			if (mc != null) {
//				embed.setThumbnail(Constants.MissingTexture);
				embed.addField("Description", mc.modInfo.desc(), true)
						.addField("Version", mc.modInfo.version(), true)
						.addField("Author(s)", mc.modInfo.author(), true);
			}
		} else {
			String mods = "";
			for (ModContainer mc : ModRegistry.mods.values()) {
				mods = String.format("%s%s, ", mods, mc.modInfo.name());
			}
			embed.setThumbnail(this.getIcon().getFile());
			embed.addField("Mods", mods);
		}
		e.message.channel.sendEmbed(embed);
	}
}
