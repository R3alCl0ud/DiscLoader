package io.discloader.discloader.core.entity;

import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.core.entity.user.UserAvatar;
import io.discloader.discloader.entity.IIcon;
import io.discloader.discloader.entity.IWebhook;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.network.json.WebhookJSON;

/**
 * @author Perry Berman
 *
 */
public class Webhook implements IWebhook {
	
	private final long id;
	private final long channel_id;
	private long guild_id = 0;
	private long user_id = 0;
	
	private String avatar;
	private String name;
	private String token;
	
	/**
	 * 
	 */
	public Webhook(WebhookJSON data) {
		id = Long.parseUnsignedLong(data.id, 10);
		channel_id = Long.parseUnsignedLong(data.channel_id, 10);
		if (data.guild_id != null) {
			guild_id = Long.parseUnsignedLong(data.guild_id, 10);
		}
		if (data.user != null) {
			user_id = EntityRegistry.addUser(data.user).getID();
		}
		
		name = data.name == null ? null : data.name;
		avatar = data.avatar == null ? null : data.avatar;
		token = data.token;
	}
	
	/*
	 * (non-Javadoc)
	 * @see io.discloader.discloader.entity.util.ISnowflake#getID()
	 */
	@Override
	public long getID() {
		return id;
	}
	
	/*
	 * (non-Javadoc)
	 * @see io.discloader.discloader.entity.IWebhook#getGuild()
	 */
	@Override
	public IGuild getGuild() {
		return guild_id == 0 ? null : EntityRegistry.getGuildByID(guild_id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see io.discloader.discloader.entity.IWebhook#getChannel()
	 */
	@Override
	public ITextChannel getChannel() {
		return EntityRegistry.getTextChannelByID(channel_id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see io.discloader.discloader.entity.IWebhook#getUser()
	 */
	@Override
	public IUser getCreator() {
		return user_id == 0 ? null : EntityRegistry.getUserByID(user_id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see io.discloader.discloader.entity.IWebhook#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	
	/*
	 * (non-Javadoc)
	 * @see io.discloader.discloader.entity.IWebhook#getAvatar()
	 */
	@Override
	public IIcon getAvatar() {
		return new UserAvatar(avatar, id, 0000);
	}
	
	/*
	 * (non-Javadoc)
	 * @see io.discloader.discloader.entity.IWebhook#getToken()
	 */
	@Override
	public String getToken() {
		return token;
	}
	
}
