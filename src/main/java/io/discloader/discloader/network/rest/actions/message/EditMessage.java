package io.discloader.discloader.network.rest.actions.message;

import java.io.File;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.core.entity.RichEmbed;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.entity.sendable.Attachment;
import io.discloader.discloader.entity.sendable.SendableMessage;
import io.discloader.discloader.network.rest.actions.RESTAction;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.network.util.Methods;

public class EditMessage<T extends ITextChannel> extends RESTAction<IMessage> {

	private IMessage message;
	private SendableMessage sendable;

	public EditMessage(IMessage message, String content, RichEmbed embed, Attachment attachment, File file) {
		super(message.getLoader());
		this.message = message;
		sendable = new SendableMessage(content, false, embed, attachment, file);
	}

	public CompletableFuture<IMessage> execute() {

		String endpoint = Endpoints.message(message.getChannel().getID(), message.getID());
		return super.execute(loader.rest.makeRequest(endpoint, Methods.PATCH, true, sendable));
	}

}