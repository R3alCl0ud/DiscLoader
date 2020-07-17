package io.discloader.discloader.network.rest.actions.channel;

import java.io.File;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.client.render.util.Resource;
import io.discloader.discloader.core.entity.RichEmbed;
import io.discloader.discloader.core.entity.message.Message;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.entity.sendable.Attachment;
import io.discloader.discloader.entity.sendable.SendableMessage;
import io.discloader.discloader.network.json.MessageJSON;
import io.discloader.discloader.network.rest.actions.RESTAction;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.network.util.Methods;

public class SendMessage<T extends ITextChannel> extends RESTAction<IMessage> {

	private SendableMessage sendable;
	private T channel;

	public SendMessage(T channel, String content, RichEmbed embed, Attachment attachment, File file) {
		super(channel.getLoader());
		sendable = new SendableMessage(content, false, embed, attachment, file);
		this.channel = channel;
	}

	public SendMessage(T channel, String content, RichEmbed embed, Attachment attachment, Resource resource) {
		super(channel.getLoader());
		sendable = new SendableMessage(content, false, embed, attachment, resource);
		this.channel = channel;
	}

	public CompletableFuture<IMessage> execute() {
		return super.execute(loader.rest.makeRequest(Endpoints.messages(channel.getID()), Methods.POST, true, sendable));
	}

	@Override
	public void complete(String r, Throwable ex) {
		if (ex != null) {
			future.completeExceptionally(ex);
			return;
		}
		future.complete(new Message<T>(channel, gson.<MessageJSON> fromJson(r, MessageJSON.class)));
	}

}
