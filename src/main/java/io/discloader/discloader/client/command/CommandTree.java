package io.discloader.discloader.client.command;

import java.util.HashMap;
import java.util.Map;

import io.discloader.discloader.common.event.message.MessageCreateEvent;

public class CommandTree extends Command {
	
	public CommandTree(String unlocalizedName) {
		super();
		setUnlocalizedName(unlocalizedName);
	}
	
	public void execute(MessageCreateEvent e, String[] args) {
		System.out.println(args[0].length());
		if (args.length == 1 && (getUnlocalizedName().equals(args[0]) || args[0].length() == 0)) {
			defaultResponse(e);
			return;
		} else if (args.length >= 1) {
			
			if (getSubCommands().containsKey(args[0])) {
				getSubCommands().get(args[0]).execute(e, slice(args));
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
		String text = "Available option(s) are:\n";
		for (Command sub : getSubCommands().values()) {
			text += (sub.getUnlocalizedName() + "\n");
		}
		e.getChannel().sendMessage(text);
		return;
	}
	
}
