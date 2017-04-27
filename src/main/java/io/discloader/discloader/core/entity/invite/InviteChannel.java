package io.discloader.discloader.core.entity.invite;

import java.time.OffsetDateTime;

import io.discloader.discloader.entity.invite.IInviteChannel;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.util.DLUtil.ChannelType;

public class InviteChannel implements IInviteChannel {

	private long id;
	private String name;
	private ChannelType type;

	public InviteChannel(ChannelJSON data) {
		id = SnowflakeUtil.parse(data.id);
		name = data.name;
		switch (data.type) {
		case 0:
			type = ChannelType.TEXT;
			break;
		case 2:
			type = ChannelType.VOICE;
			break;
		default:
			type = ChannelType.CHANNEL;
			break;
		}
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public OffsetDateTime createdAt() {
		return SnowflakeUtil.creationTime(this);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ChannelType getType() {
		return type;
	}

}
