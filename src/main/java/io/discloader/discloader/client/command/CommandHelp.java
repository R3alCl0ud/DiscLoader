package io.discloader.discloader.client.command;

import java.io.File;
import java.util.List;
import java.util.Locale;

import com.google.common.collect.Lists;

import io.discloader.discloader.client.render.util.Resource;
import io.discloader.discloader.common.event.message.MessageCreateEvent;
import io.discloader.discloader.common.language.LanguageRegistry;
import io.discloader.discloader.common.registry.CommandRegistry;
import io.discloader.discloader.core.entity.RichEmbed;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.util.DLUtil;

/**
 * @author Perry Berman
 */
public class CommandHelp extends Command {
	
	public CommandHelp() {
		super();
		setTextureName("discloader:help").setDescription("Displays information about the available commands").setUsage("help [<command>]");
	}
	
	@Override
	public void execute(MessageCreateEvent e, String[] args) {
		IMessage message = e.getMessage();
		RichEmbed embed = new RichEmbed().setFooter(String.format("type `%shelp <page>` to tab through the pages", CommandHandler.prefix), e.loader.user.getAvatar().toString())
				.setAuthor(e.loader.user.getUsername(), "http://discloader.io", e.loader.user.getAvatar().toString()).setColor(0x08a2ff);
		Command command;
		embed.setThumbnail(getResourceLocation());
		if (args.length >= 1 && (command = CommandHandler.getCommand(args[0], message)) != null) {
			if (command != null) {
				File icon = DLUtil.MissingTexture;
				embed.setThumbnail(command.getResourceLocation());
				if (embed.getThumbnail() == null) embed.setThumbnail(icon);
				if (args.length > 1 && command instanceof CommandTree) {
					for (int i = 1; i < args.length; i++) {
						if (((CommandTree) command).getSubCommands().get(args[i]) != null) command = ((CommandTree) command).getSubCommands().get(args[i]);
					}
				}
				
				embed.setTitle(command.getUnlocalizedName()).addField("Description", this.getCommandDesc(command), true).addField("Usage", command.getUsage(), true);
				if (command instanceof CommandTree) {
					String commands = "";
					for (Command sub : ((CommandTree) command).getSubCommands().values()) {
						String desc = this.getCommandDesc(sub);
						commands = String.format("%s**%s**: %s\n", commands, sub.getUnlocalizedName(), desc);
					}
					embed.addField("Sub Commands", commands, true);
				}
				e.getChannel().sendEmbed(embed);
				return;
			}
		} else if (args.length == 1 && args[0] != null && args[0].length() > 0) {
			String commands = "";
			int page = Integer.parseInt(args[0], 10);
			int size = CommandRegistry.commands.entries().size();
			List<Command> cmdList = Lists.newArrayList(CommandRegistry.commands.entries().toArray(new Command[size]));
			cmdList.sort((a, b) -> {
				if (a.getUnlocalizedName().compareToIgnoreCase(b.getUnlocalizedName()) < 0) return -1;
				if (a.getUnlocalizedName().compareToIgnoreCase(b.getUnlocalizedName()) > 0) return 1;
				return 0;
			});
			for (int i = 0 + (10 * (page - 1)); i < (10 * page) && i < size; i++) {
				String desc = this.getCommandDesc(cmdList.get(i));
				commands = String.format("%s**%s**: %s\n", commands, cmdList.get(i).getUnlocalizedName(), desc);
			}
			embed.addField("Commands", commands, true);
			embed.setTitle(String.format("Help. Page: %d/%d", page, (size / 10) + (size % 10 != 0 ? 1 : 0)));
		} else {
			String commands = "";
			int size = CommandRegistry.commands.entries().size();
			List<Command> cmdList = Lists.newArrayList(CommandRegistry.commands.entries().toArray(new Command[size]));
			cmdList.sort((a, b) -> {
				if (a.getUnlocalizedName().compareToIgnoreCase(b.getUnlocalizedName()) < 0) return -1;
				if (a.getUnlocalizedName().compareToIgnoreCase(b.getUnlocalizedName()) > 0) return 1;
				return 0;
			});
			for (int i = 0; i < 10 && i < size; i++) {
				String desc = this.getCommandDesc(cmdList.get(i));
				commands = String.format("%s**%s**: %s\n", commands, cmdList.get(i).getUnlocalizedName(), desc);
			}
			embed.addField("Commands", commands, true);
			embed.setTitle(String.format("Help. Page: 1/%d", (size / 10) + (size % 10 != 0 ? 1 : 0)));
		}
		e.getChannel().sendEmbed(embed);
	}
	
	private String getCommandDesc(Command command) {
		String desc = LanguageRegistry.getLocalized(Locale.US, "command." + command.getUnlocalizedName() + ".desc");
		if (desc == null || desc.length() < 1) {
			return command.getDescription();
		}
		return desc;
	}
	
	public Resource getResourceLocation() {
		return new Resource("discloader", "texture/commands/help.png");
	}
	
}
