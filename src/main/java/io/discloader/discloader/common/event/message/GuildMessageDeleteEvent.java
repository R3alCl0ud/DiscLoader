package io.discloader.discloader.common.event.message;

import io.discloader.discloader.entity.channel.IGuildTextChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.message.IMessage;

public class GuildMessageDeleteEvent extends MessageDeleteEvent {

	// private IGuild guild;

	public GuildMessageDeleteEvent(IMessage message) {
		super(message);
		// guild = message.getGuild();
	}

	public IGuild getGuild() {
		return getMessage().getGuild();
	}

	@Override
	public IGuildTextChannel getChannel() {
		return (IGuildTextChannel) super.getChannel();
	}

}
