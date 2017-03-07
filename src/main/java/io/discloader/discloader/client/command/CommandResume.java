package io.discloader.discloader.client.command;

import io.discloader.discloader.common.event.MessageCreateEvent;

public class CommandResume extends Command {
	public void execute(MessageCreateEvent e) {
		e.loader.socket.ws.disconnect();
	}
}
