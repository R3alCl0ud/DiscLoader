package io.discloader.discloader.core.entity.channel;

import java.time.OffsetDateTime;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.channel.ChannelTypes;
import io.discloader.discloader.entity.channel.IChannel;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.json.ChannelJSON;

public class Channel implements IChannel {

	/**
	 * The channel's Snowflake ID.
	 */
	private long id;

	protected short type;

	/**
	 * The current instance of DiscLoader
	 */
	protected final DiscLoader loader;

	public Channel(DiscLoader loader, ChannelJSON data) {
		this.loader = loader;

		id = SnowflakeUtil.parse(data.id);

		type = data.type;

		if (data != null)
			setup(data);
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public DiscLoader getLoader() {
		return loader;
	}

	@Override
	public ChannelTypes getType() {
		return ChannelTypes.fromCode(type);
	}

	@Override
	public boolean isPrivate() {
		return getType() == ChannelTypes.DM || getType() == ChannelTypes.GROUP;
	}

	public void setup(ChannelJSON data) {

	}

	public String toMention() {
		return String.format("<#%s>", id);
	}

	@Override
	public OffsetDateTime createdAt() {
		return SnowflakeUtil.creationTime(this);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Channel))
			return false;

		Channel c = (Channel) o;
		return this == c || getID() == c.getID();
	}

	@Override
	public int hashCode() {
		return Long.hashCode(getID());
	}
}
