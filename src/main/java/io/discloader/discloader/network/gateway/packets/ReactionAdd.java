package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.message.MessageReactionAddEvent;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.core.entity.message.Reaction;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.entity.message.IReaction;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.network.gateway.Gateway;
import io.discloader.discloader.network.json.ReactionJSON;

public class ReactionAdd extends AbstractHandler {

	public ReactionAdd(Gateway socket) {
		super(socket);
	}

	public void handle(SocketPacket packet) {
		ReactionJSON data = gson.fromJson(gson.toJson(packet.d), ReactionJSON.class);
		IUser user = EntityRegistry.getUserByID(data.user_id);
		ITextChannel channel = EntityRegistry.getTextChannelByID(data.channel_id);
		if (channel == null) channel = EntityRegistry.getPrivateChannelByID(data.channel_id);
		if (channel == null) return;
		IMessage message = channel.getMessage(data.message_id);
		if (message == null || user == null) return;
		IReaction reaction = new Reaction(data, message);
		message.getReactions().add(reaction);
		loader.emit(new MessageReactionAddEvent(message, reaction, user));
	}
}
