package io.discloader.discloader.rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import com.google.gson.Gson;

import io.discloader.discloader.DiscLoader;
import io.discloader.discloader.objects.gateway.GuildJSON;
import io.discloader.discloader.objects.gateway.MemberJSON;
import io.discloader.discloader.objects.gateway.MessageJSON;
import io.discloader.discloader.objects.gateway.UserJSON;
import io.discloader.discloader.objects.structures.Guild;
import io.discloader.discloader.objects.structures.GuildMember;
import io.discloader.discloader.objects.structures.Message;
import io.discloader.discloader.objects.structures.RichEmbed;
import io.discloader.discloader.objects.structures.User;
import io.discloader.discloader.objects.structures.channels.TextChannel;
import io.discloader.discloader.util.constants;

public class DiscREST {
	public Gson gson;
	public HashMap<String, DiscRESTQueue> queues;
	public DiscLoader loader;

	public DiscREST(DiscLoader loader) {
		this.loader = loader;

		this.gson = new Gson();

		this.queues = new HashMap<String, DiscRESTQueue>();
	}

	public void handleQueue(String route) {
		this.queues.get(route).handle();
	}

	public CompletableFuture<String> makeRequest(String url, int method, boolean auth, Object data) {
		APIRequest request = new APIRequest(url, method, auth, data);
		CompletableFuture<String> future = new CompletableFuture<String>();
		if (!this.queues.containsKey(url)) {
			this.queues.put(url, new DiscRESTQueue(this));
		}

		request.setFuture(future);
		this.queues.get(url).addToQueue(request);
		this.handleQueue(url);
		return future;
	}

	public CompletableFuture<String> makeRequest(String url, int method, boolean auth) {
		return this.makeRequest(url, method, auth, null);
	}

	public CompletableFuture<Message> sendMessage(TextChannel channel, String content, RichEmbed embed) {
		if (content.length() < 1)
			return null;
		CompletableFuture<Message> msgSent = new CompletableFuture<Message>();
		this.makeRequest(constants.Endpoints.messages(channel.id), constants.Methods.POST, true,
				new JSONObject().put("content", content).toString()).thenAcceptAsync(action -> {
					msgSent.complete(new Message(channel, this.gson.fromJson(action, MessageJSON.class)));
				});
		return msgSent;
	}

	public CompletableFuture<Message> editMessage(TextChannel channel, Message message, String content, RichEmbed embed) {
		if (content.length() < 1)
			return null;
		CompletableFuture<Message> future = new CompletableFuture<Message>();
		this.makeRequest(constants.Endpoints.message(channel.id, message.id), constants.Methods.PATCH, true,
				new JSONObject().put("content", content).put("embed", embed).toString()).thenAcceptAsync(action -> {
					future.complete(new Message(channel, this.gson.fromJson(action, MessageJSON.class)));
				});
		return future;
	}

	public CompletableFuture<Message> deleteMessage(TextChannel channel, Message message) {
		CompletableFuture<Message> future = new CompletableFuture<Message>();
		this.makeRequest(constants.Endpoints.message(channel.id, message.id), constants.Methods.DELETE, true)
				.thenAcceptAsync(action -> {
					future.complete(message);
				});
		return future;
	}

	public CompletableFuture<User> setUsername(String username) {
		CompletableFuture<User> future = new CompletableFuture<User>();
		this.makeRequest(constants.Endpoints.currentUser, constants.Methods.PATCH, true,
				new JSONObject().put("username", username)).thenAcceptAsync(action -> {
					future.complete(this.loader.user.patch(this.gson.fromJson(action, UserJSON.class)));
				});
		return future;
	}

	public CompletableFuture<User> setAvatar(String avatar) {
		CompletableFuture<User> future = new CompletableFuture<User>();
		try {
			String base64 = new String(
					"data:image/jpg;base64," + Base64.encodeBase64String(Files.readAllBytes(Paths.get(avatar))));
			this.makeRequest(constants.Endpoints.currentUser, constants.Methods.PATCH, true,
					new JSONObject().put("avatar", base64)).thenAcceptAsync(action -> {
						future.complete(this.loader.user.patch(this.gson.fromJson(action, UserJSON.class)));
					});
		} catch (IOException e) {
			future.completeExceptionally(e);
		}
		return future;
	}

	public CompletableFuture<GuildMember> setNick(GuildMember member, String nick) {
		CompletableFuture<GuildMember> future = new CompletableFuture<GuildMember>();
		String endpoint = member.id == this.loader.user.id ? constants.Endpoints.guildNick(member.guild.id)
				: constants.Endpoints.guildMember(member.guild.id, member.id);
		this.makeRequest(endpoint, constants.Methods.PATCH, true, new JSONObject().put("nick", nick))
				.thenAcceptAsync(action -> {
					member.nick = nick;
					future.complete(member);
				});
		return future;
	}

	public CompletableFuture<Guild> modifyGuild(Guild guild, JSONObject data) {
		CompletableFuture<Guild> future = new CompletableFuture<Guild>();
		this.makeRequest(constants.Endpoints.guild(guild.id), constants.Methods.PATCH, true, data)
				.thenAcceptAsync(action -> {
					guild.setup(this.gson.fromJson(action, GuildJSON.class));
					future.complete(guild);
				});
		return future;
	}

	public CompletableFuture<GuildMember> loadGuildMember(Guild guild, String memberID) {
		CompletableFuture<GuildMember> future = new CompletableFuture<GuildMember>();
		this.makeRequest(constants.Endpoints.guildMember(guild.id, memberID), constants.Methods.GET, true)
				.thenAcceptAsync(action -> {
					future.complete(guild.addMember(this.gson.fromJson(action, MemberJSON.class)));
				});
		return future;
	}

}
