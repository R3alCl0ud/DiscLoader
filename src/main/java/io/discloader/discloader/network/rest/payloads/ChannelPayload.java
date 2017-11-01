package io.discloader.discloader.network.rest.payloads;

import io.discloader.discloader.entity.IOverwrite;
import io.discloader.discloader.entity.channel.ChannelTypes;
import io.discloader.discloader.entity.channel.IChannelCategory;
import io.discloader.discloader.entity.util.SnowflakeUtil;

public class ChannelPayload {

	private String name, parent_id;
	private int type, bitrate, user_limit;
	private boolean nsfw;
	private IOverwrite[] overwrites;

	public ChannelPayload(String name) {
		this.name = name;
		this.type = 0;
	}

	public ChannelPayload(String name, ChannelTypes type) {
		this.name = name;
		setType(type);
	}

	public ChannelPayload(String name, ChannelTypes type, IOverwrite... overwrites) {
		this.name = name;
		setType(type);
		setOverwrites(overwrites);
	}

	public int getBitrate() {
		return bitrate;
	}

	public String getName() {
		return name;
	}

	public String getParent_id() {
		return parent_id;
	}

	public int getType() {
		return type;
	}

	public int getUser_limit() {
		return user_limit;
	}

	public boolean isNsfw() {
		return nsfw;
	}

	public void setBitrate(int bitrate) {
		this.bitrate = bitrate;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNsfw(boolean nsfw) {
		this.nsfw = nsfw;
	}

	public void setParent(IChannelCategory parent) {
		this.parent_id = SnowflakeUtil.asString(parent);
	}

	public void setType(ChannelTypes type) {
		this.type = type.toInt();
	}

	public void setUserLimit(int user_limit) {
		this.user_limit = user_limit;
	}

	public IOverwrite[] getOverwrites() {
		return overwrites;
	}

	public void setOverwrites(IOverwrite... overwrites) {
		this.overwrites = overwrites;
	}
}
