package io.discloader.discloader.common.event.message;

import io.discloader.discloader.common.entity.channel.TextChannel;
import io.discloader.discloader.common.entity.guild.Guild;
import io.discloader.discloader.entity.message.Message;

public class GuildMessageUpdateEvent extends MessageUpdateEvent {

	private Guild guild;

	public GuildMessageUpdateEvent(Message message, Message oldMessage) {
		super(message, oldMessage);
		guild = message.guild;
	}

	/**
	 * @return the guild
	 */
	public Guild getGuild() {
		return guild;
	}

	@Override
	public TextChannel getChannel() {
		return (TextChannel) super.getChannel();
	}

}
