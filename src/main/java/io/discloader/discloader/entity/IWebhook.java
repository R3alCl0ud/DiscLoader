package io.discloader.discloader.entity;

import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.util.ISnowflake;

public interface IWebhook extends ISnowflake {
	
	IGuild getGuild();
	
	ITextChannel getChannel();
	
	IUser getCreator();
	
	String getName();
	
	IIcon getAvatar();
	
	String getToken();
	
}
