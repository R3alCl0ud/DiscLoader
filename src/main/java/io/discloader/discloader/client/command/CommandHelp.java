package io.discloader.discloader.client.command;

import java.io.File;
import java.util.Locale;

import io.discloader.discloader.client.render.texture.icon.CommandIcon;
import io.discloader.discloader.client.render.util.IIcon;
import io.discloader.discloader.common.event.message.MessageCreateEvent;
import io.discloader.discloader.common.language.LanguageRegistry;
import io.discloader.discloader.common.registry.CommandRegistry;
import io.discloader.discloader.entity.RichEmbed;
import io.discloader.discloader.entity.message.Message;
import io.discloader.discloader.util.DLUtil;

/**
 * 
 * 
 * @author Perry Berman
 */
public class CommandHelp extends Command {

	public CommandHelp() {
		super();
		setTextureName("discloader:help").setDescription("Displays information about the available commands")
				.setUsage("help [<command>]");
	}

	public void execute(MessageCreateEvent e, String[] args) {
		Message message = e.getMessage();
		RichEmbed embed = new RichEmbed()
				.setFooter(String.format("type `%shelp <page>` to tab threw the pages", CommandHandler.prefix),
						e.loader.user.getAvatar().toString())
				.setAuthor(e.loader.user.username, "http://discloader.io", e.loader.user.getAvatar().toString())
				.setColor(0x08a2ff);
		Command command;
		embed.setThumbnail(this.getIcon().getFile());
		if (args.length == 1 && (command = CommandHandler.getCommand(args[0], message)) != null) {
			if (command != null) {
				File icon = DLUtil.MissingTexture;
				IIcon iicon = (CommandIcon) command.getIcon();

				if (iicon != null && iicon.getFile() != null) {
					icon = iicon.getFile();
				}

				embed.setTitle(command.getUnlocalizedName()).setThumbnail(icon)
						.addField("Description", this.getCommandDesc(command), true)
						.addField("Usage", command.getUsage(), true);
			}
		} else if (args.length == 1 && args[0].length() > 0) {
			String commands = "";
			int page = Integer.parseInt(args[0], 10);
			int size = CommandRegistry.commands.entries().size();
			Command[] cmds = CommandRegistry.commands.entries().toArray(new Command[size]);
			for (int i = 0 + (10 * page); i < (10 * (page + 1)) && i < size; i++) {
				String desc = this.getCommandDesc(cmds[i]);
				commands = String.format("%s**%s**: %s\n", commands, cmds[i].getUnlocalizedName(), desc);
			}
			embed.addField("Commands", commands, true);
			embed.setTitle(String.format("Help. Page: 1/%d", (size / 10) + size % 10 != 0 ? 1 : 0));
		} else {
			String commands = "";
			int size = CommandRegistry.commands.entries().size();
			Command[] cmds = CommandRegistry.commands.entries().toArray(new Command[size]);
			for (int i = 0; i < 10 && i < cmds.length; i++) {
				String desc = this.getCommandDesc(cmds[i]);
				commands = String.format("%s**%s**: %s\n", commands, cmds[i].getUnlocalizedName(), desc);
			}
			embed.addField("Commands", commands, true);
			embed.setTitle(String.format("Help. Page: 1/%d", (size / 10) + size % 10 != 0 ? 1 : 0));
		}
		message.channel.sendEmbed(embed);
	}

	private String getCommandDesc(Command command) {
		String desc = LanguageRegistry.getLocalized(Locale.US, "command", command.getUnlocalizedName(), "desc");
		if (desc == null || desc.length() < 1) {
			return command.getDescription();
		}
		return desc;
	}

}
