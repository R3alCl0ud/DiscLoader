package io.discloader.discloader.common.event.message;

import io.discloader.discloader.core.entity.channel.TextChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.message.IMessage;

public class GuildMessageDeleteEvent extends MessageDeleteEvent {

	private IGuild guild;

	public GuildMessageDeleteEvent(IMessage message) {
		super(message);
		guild = message.getGuild();
	}

	/**
	 * @return the guild
	 */
	public IGuild getGuild() {
		return guild;
	}

	@Override
	public TextChannel getChannel() {
		return (TextChannel) super.getChannel();
	}

}
