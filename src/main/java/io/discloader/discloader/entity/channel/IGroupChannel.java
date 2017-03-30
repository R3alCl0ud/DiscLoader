package io.discloader.discloader.entity.channel;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.entity.user.IUser;

public interface IGroupChannel extends ITextChannel {

	Map<String, IUser> getRecipients();

	<T extends ITextChannel> CompletableFuture<IMessage<T>> pinMessage(IMessage<T> message);

}
