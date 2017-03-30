package io.discloader.discloader.common.event.message;

import io.discloader.discloader.core.entity.channel.TextChannel;
import io.discloader.discloader.core.entity.message.Message;
import io.discloader.discloader.entity.guild.IGuild;

public class GuildMessageDeleteEvent extends MessageDeleteEvent {

	private IGuild guild;

	public GuildMessageDeleteEvent(Message message) {
		super(message);
		guild = message.guild;
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
