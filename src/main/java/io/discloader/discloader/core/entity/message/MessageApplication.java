package io.discloader.discloader.core.entity.message;

import io.discloader.discloader.entity.message.IMessageApplication;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.json.MessageApplicationJSON;

public class MessageApplication implements IMessageApplication {

	private final long id;
	private String name, icon, description, coverImage;

	public MessageApplication(MessageApplicationJSON data) {
		this.id = SnowflakeUtil.parse(data.id);
		this.name = data.name;
		this.icon = data.icon;
		this.description = data.description;
		this.coverImage = data.cover_image;
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getIcon() {
		return icon;
	}

	@Override
	public String getCoverImage() {
		return coverImage;
	}

	@Override
	public int hashCode() {
		return Long.hashCode(getID());
	}
}
