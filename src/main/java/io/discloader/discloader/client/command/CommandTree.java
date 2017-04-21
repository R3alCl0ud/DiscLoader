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
		if (args.length == 0) {
			defaultResponse(e);
			return;
		} else if (args.length >= 1) {

		}
	}

	public Map<String, Command> getSubCommands() {
		return new HashMap<>();
	}

	public void defaultResponse(MessageCreateEvent e) {
		return;
	}

}
