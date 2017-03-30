package io.discloader.discloader.entity.message;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.core.entity.RichEmbed;
import io.discloader.discloader.entity.ISnowflake;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.guild.IGuild;

/**
 * @author Perry Berman
 *
 */
public interface IMessage extends ISnowflake {
	
	boolean canDelete();
	
	boolean canEdit();
	
	CompletableFuture<IMessage> delete();
	
	CompletableFuture<IMessage> edit(RichEmbed embed);
	
	CompletableFuture<IMessage> edit(String content);
	
	CompletableFuture<IMessage> edit(String content, RichEmbed embed);
	
	ITextChannel getChannel();
	
	String getContent();
	
	IGuild getGuild();
	
	boolean isPinned();
	
	boolean isSystem();
	
	boolean isTTS();
	
	CompletableFuture<IMessage> pin();
	
	void setup();
	
	CompletableFuture<IMessage> unpin();
}
