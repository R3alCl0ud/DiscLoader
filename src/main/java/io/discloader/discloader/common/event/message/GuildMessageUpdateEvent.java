package io.discloader.discloader.common.event.message;

import io.discloader.discloader.entity.channel.IGuildTextChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.message.IMessage;

public class GuildMessageUpdateEvent extends MessageUpdateEvent {

	public GuildMessageUpdateEvent(IMessage message, IMessage oldMessage) {
		super(message, oldMessage);
	}

	/**
	 * @return the guild
	 */
	public IGuild getGuild() {
		return getMessage().getGuild();
	}

	@Override
	public IGuildTextChannel getChannel() {
		return (IGuildTextChannel) super.getChannel();
	}

}
