package io.discloader.discloader.entity.message;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.core.entity.RichEmbed;
import io.discloader.discloader.entity.ISnowflake;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.network.json.MessageJSON;

/**
 * @author Perry Berman
 */
public interface IMessage extends ISnowflake {
	
	boolean canDelete();
	
	boolean canEdit();
	
	CompletableFuture<IMessage> delete();
	
	CompletableFuture<IMessage> edit(RichEmbed embed);
	
	CompletableFuture<IMessage> edit(String content);
	
	CompletableFuture<IMessage> edit(String content, RichEmbed embed);
	
	IUser getAuthor();
	
	ITextChannel getChannel();
	
	String getContent();
	
	Date getEditedTimestamp();
	
	IGuild getGuild();
	
	DiscLoader getLoader();
	
	String getNonce();
	
	Date getTimestamp();
	
	boolean isPinned();
	
	boolean isSystem();
	
	boolean isTTS();
	
	CompletableFuture<IMessage> pin();
	
	void setup(MessageJSON data);
	
	CompletableFuture<IMessage> unpin();
}
