package io.discloader.discloader.client.command;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import io.discloader.discloader.common.event.message.MessageCreateEvent;
import io.discloader.discloader.entity.channel.IGuildTextChannel;

public class CommandTree extends Command {
	
	protected Map<String, Command> subCommands = new HashMap<>();
	
	public CommandTree(String unlocalizedName) {
		super();
		setUnlocalizedName(unlocalizedName);
	}

	@Override
	public void execute(MessageCreateEvent e, String[] args) throws Exception {
		if (args.length == 0) {
			defaultResponse(e);
			return;
		} else if (args.length >= 1) {
			for (Command cmd : getSubCommands().values()) {
				if (cmd.getUnlocalizedName().equalsIgnoreCase(args[0]) || cmd.getAliases().contains(args[0])) {
					String[] argv = slice(args), argc = new String[args.length - 1];
					String rest = "";
					for (String arg : argv)
						rest += (arg + " ");
					Matcher argM = cmd.getArgsPattern().matcher(rest);
					if (argM.find()) {
						argc = new String[argM.groupCount()];
						for (int i = 0; i < argM.groupCount(); i++) {
							try {
								argc[i] = argM.group(i + 1);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}
					if (e.getMessage().getGuild() != null && !cmd.shouldExecute(e.getMessage().getMember(), (IGuildTextChannel) e.getChannel())) {
						return;
					} else {
						cmd.execute(e, argc);
						return;
					}
				}
			}
			defaultResponse(e);
		}
	}

	private String[] slice(String[] strings) {
		if (strings.length <= 1)
			return new String[0];
		String[] b = new String[strings.length - 1];
		for (int i = 1; i < strings.length; i++)
			b[i - 1] = strings[i];
		return b;
	}

	public Map<String, Command> getSubCommands() {
		return subCommands;
	}

	public void defaultResponse(MessageCreateEvent e) {
		String text = "Available option(s) are:\n" + this.subsText(this, 0);
		e.getChannel().sendMessage(text);
		return;
	}

	protected String subsText(CommandTree cmdt, int l) {
		String text = "";
		for (Command sub : getSubCommands().values()) {
			if (sub instanceof CommandTree)
				text += subsText((CommandTree) sub, l + 1);
			else
				text += (sub.getUnlocalizedName() + "\n");
		}
		return text;
	}

}
