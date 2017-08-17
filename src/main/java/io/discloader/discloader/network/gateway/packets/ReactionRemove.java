package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.message.MessageReactionRemoveEvent;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.core.entity.message.Reaction;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.entity.message.IReaction;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.network.gateway.Gateway;
import io.discloader.discloader.network.json.ReactionJSON;

public class ReactionRemove extends AbstractHandler {

	public ReactionRemove(Gateway socket) {
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
		for (IReaction r : message.getReactions()) {
			if (r.getEmoji().toString().equals(reaction.toString())) reaction = r;
		}
		message.getReactions().remove(reaction);
		loader.emit(new MessageReactionRemoveEvent(message, reaction, user));
	}
}
