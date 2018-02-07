package io.discloader.discloader.core.entity.message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.core.entity.guild.GuildEmoji;
import io.discloader.discloader.entity.IEmoji;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.entity.message.IReaction;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.json.ReactionJSON;
import io.discloader.discloader.network.rest.RESTOptions;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.network.util.Methods;

public class Reaction implements IReaction {

	private boolean me;
	private int count;
	private IEmoji emoji;
	private IMessage message;

	public Reaction(ReactionJSON data, IMessage message) {
		this.message = message;
		me = data.me;
		count = data.count;
		if (data.emoji.id != null)
			emoji = new GuildEmoji(data.emoji, message.getGuild());
		else
			emoji = new Emoji(data.emoji, message.getLoader());
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @return the emoji
	 */
	public IEmoji getEmoji() {
		return emoji;
	}

	public IMessage getMessage() {
		return message;
	}

	@Override
	public boolean didReact() {
		return me;
	}

	@Override
	public CompletableFuture<List<IUser>> getUsers() {
		CompletableFuture<List<IUser>> future = new CompletableFuture<>();
		CompletableFuture<IUser[]> cf = message.getLoader().rest.request(Methods.GET, Endpoints.messageReaction(message.getChannel().getID(), message.getID(), SnowflakeUtil.asString(emoji)), new RESTOptions(), IUser[].class);
		cf.thenAcceptAsync(ius -> {
			List<IUser> users = new ArrayList<>();
			for (IUser usr : ius) {
				users.add(usr);
			}
			future.complete(users);
		});
		return future;
		// return new RESTAction<List<IUser>>(message.getLoader()) {
		//
		// @Override
		// public CompletableFuture<List<IUser>> execute() {
		// return
		// super.execute(loader.rest.makeRequest(Endpoints.messageReaction(message.getChannel().getID(),
		// message.getID(), SnowflakeUtil.asString(emoji)), Methods.GET, true));
		// }
		//
		// @Override
		// public void complete(String text, Throwable ex) {
		// if (ex != null) {
		// future.completeExceptionally(ex);
		// return;
		// }
		// }
		// }.execute();
	}

	@Override
	public CompletableFuture<Void> removeUserReaction(IUser user) {
		return message.getLoader().rest.request(Methods.DELETE, Endpoints.userReaction(message.getChannel().getID(), message.getID(), user.getID(), SnowflakeUtil.asString(emoji)), new RESTOptions(), Void.class);
	}

	@Override
	public CompletableFuture<Void> removeReaction() {
		return message.getLoader().rest.request(Methods.DELETE, Endpoints.currentUserReaction(message.getChannel().getID(), message.getID(), SnowflakeUtil.asString(emoji)), new RESTOptions(), Void.class);
	}
}
