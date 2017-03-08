package io.discloader.discloader.client.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.discloader.discloader.common.discovery.ModContainer;
import io.discloader.discloader.common.event.MessageCreateEvent;
import io.discloader.discloader.common.registry.ModRegistry;
import io.discloader.discloader.entity.RichEmbed;
import io.discloader.discloader.util.DLUtil;

/**
 * @author Perry Berman
 *
 */
public class CommandMods extends Command {

	private final String regex = "\\/\\/mods (.*)";
	private final Pattern pattern = Pattern.compile(regex);

	public CommandMods() {
		super();
		this.setUnlocalizedName("mods").setTextureName("discloader:mods").setDescription("mods")
				.setUsage("mods [<mod>]");
	}

	public void execute(MessageCreateEvent e) {
		RichEmbed embed = new RichEmbed().setColor(0x55cdF2);
		Matcher modMatch = pattern.matcher(e.message.content);
		if (modMatch.find()) {
			String mod = modMatch.group(1);
			System.out.println(mod);
		}

		if (e.args.length == 2) {
			String mod = e.args[1];
			System.out.println(mod);
			ModContainer mc = ModRegistry.mods.get(mod);
			if (mc != null) {
				embed.setThumbnail(DLUtil.MissingTexture);
				embed.addField("Description", mc.modInfo.desc(), true).addField("Version", mc.modInfo.version(), true)
						.addField("Author(s)", mc.modInfo.author(), true);
			}
		} else {
			ArrayList<String> modList = new ArrayList<String>();
			for (ModContainer mc : ModRegistry.mods.values()) {
				modList.add(String.format("%s", mc.modInfo.name()));
			}
			embed.setThumbnail(this.getIcon().getFile());
			String mods = Arrays.toString(modList.toArray());
			mods = mods.substring(1, mods.length() - 1);
			embed.addField("Mods", mods);
		}
		e.message.channel.sendEmbed(embed);
	}
}
