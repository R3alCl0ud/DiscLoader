package io.discloader.discloader.client.command;

import java.util.ArrayList;
import java.util.Arrays;

import io.discloader.discloader.common.discovery.ModContainer;
import io.discloader.discloader.common.event.message.MessageCreateEvent;
import io.discloader.discloader.common.registry.ModRegistry;
import io.discloader.discloader.core.entity.RichEmbed;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.util.DLUtil;

/**
 * @author Perry Berman
 *
 */
public class CommandMods extends Command {

	private final String regex = "(.*)";

	public CommandMods() {
		super();
		setUnlocalizedName("mods").setTextureName("discloader:mods").setDescription("mods");
		setUsage("mods [<mod>]").setArgsRegex(regex);
	}

	@Override
	public void execute(MessageCreateEvent e, String[] args) {
		IMessage message = e.getMessage();
		RichEmbed embed = new RichEmbed().setColor(0x55cdF2);
		ModContainer mc;
		try {
			if (args.length == 1 && (mc = ModRegistry.mods.get(args[0])) != null) {
				if (mc != null) {
					embed.setThumbnail(DLUtil.MissingTexture);
					embed.addField("Description", mc.modInfo.desc(), true).addField("Version", mc.modInfo.version(), true).addField("Author(s)", mc.modInfo.author(), true);
				}
			} else {
				ArrayList<String> modList = new ArrayList<String>();
				if (!ModRegistry.mods.isEmpty()) {
					for (ModContainer mcs : ModRegistry.mods.values()) {
						modList.add(String.format("%s", mcs.modInfo.name()));
					}
				} else {
					modList.add("No Mods");
				}
				embed.setThumbnail(getResourceLocation());
				String mods = Arrays.toString(modList.toArray());
				mods = mods.substring(1, mods.length() - 1);
				embed.addField("Mods", mods);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		message.getChannel().sendEmbed(embed);
	}
}
