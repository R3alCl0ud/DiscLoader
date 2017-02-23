package io.discloader.discloader.entity.impl;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.Message;
import io.discloader.discloader.entity.RichEmbed;

/**
 * @author perryberman
 * @since 0.0.3
 */
public interface ITextChannel {

	CompletableFuture<Message> sendMessage(String content, RichEmbed embed, boolean tts);

	CompletableFuture<Message> sendMessage(String content, RichEmbed embed);

	CompletableFuture<Message> sendMessage(String content, boolean tts);

	CompletableFuture<Message> sendMessage(String content);

	CompletableFuture<Message> sendEmbed(RichEmbed embed);
}
