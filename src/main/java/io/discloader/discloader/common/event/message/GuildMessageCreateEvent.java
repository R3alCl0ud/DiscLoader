package io.discloader.discloader.common.event.message;

import io.discloader.discloader.entity.channel.IGuildTextChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.message.IMessage;

public class GuildMessageCreateEvent extends MessageCreateEvent {

	private IGuild guild;

	public GuildMessageCreateEvent(IMessage message) {
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
	public IGuildTextChannel getChannel() {
		return (IGuildTextChannel) super.getChannel();
	}

}
