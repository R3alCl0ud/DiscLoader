package io.discloader.discloader.client.command;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import io.discloader.discloader.common.event.message.MessageCreateEvent;
import io.discloader.discloader.entity.channel.IGuildTextChannel;

public class CommandTree extends Command {
	
	public CommandTree(String unlocalizedName) {
		super();
		setUnlocalizedName(unlocalizedName);
	}
	
	public void execute(MessageCreateEvent e, String[] args) {
		if (args.length == 0) {
			defaultResponse(e);
			return;
		} else if (args.length >= 1) {
			if (getSubCommands().containsKey(args[0])) {
				String[] argv = slice(args), argc = new String[args.length - 1];
				String rest = "";
				for (String arg : argv)
					rest += (arg + " ");
				Matcher argM = getSubCommands().get(args[0]).getArgsPattern().matcher(rest);
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
				if (getSubCommands().get(args[0]).shouldExecute(e.getMessage().getMember(), (IGuildTextChannel) e.getChannel())) getSubCommands().get(args[0]).execute(e, argc);
			}
		}
	}
	
	private String[] slice(String[] strings) {
		if (strings.length <= 1) return new String[0];
		String[] b = new String[strings.length - 1];
		for (int i = 1; i < strings.length; i++)
			b[i - 1] = strings[i];
		return b;
	}
	
	public Map<String, Command> getSubCommands() {
		return new HashMap<>();
	}
	
	public void defaultResponse(MessageCreateEvent e) {
		String text = "Available option(s) are:\n" + this.subsText(this, 0);
		e.getChannel().sendMessage(text);
		return;
	}
	
	private String subsText(CommandTree cmdt, int l) {
		String text = "";
		for (Command sub : getSubCommands().values()) {
			if (sub instanceof CommandTree) text += subsText((CommandTree) sub, l + 1);
			else text += (sub.getUnlocalizedName() + "\n");
		}
		return text;
	}
	
}
