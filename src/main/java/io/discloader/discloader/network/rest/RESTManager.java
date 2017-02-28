package io.discloader.discloader.network.rest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import com.google.gson.Gson;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.Attachment;
import io.discloader.discloader.entity.Guild;
import io.discloader.discloader.entity.GuildMember;
import io.discloader.discloader.entity.Message;
import io.discloader.discloader.entity.OAuth2Application;
import io.discloader.discloader.entity.User;
import io.discloader.discloader.entity.channels.VoiceChannel;
import io.discloader.discloader.entity.impl.ITextChannel;
import io.discloader.discloader.entity.sendable.RichEmbed;
import io.discloader.discloader.entity.sendable.SendableMessage;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.json.GuildJSON;
import io.discloader.discloader.network.json.MemberJSON;
import io.discloader.discloader.network.json.MessageJSON;
import io.discloader.discloader.network.json.OAuthApplicationJSON;
import io.discloader.discloader.network.json.UserJSON;
import io.discloader.discloader.util.Constants;

public class RESTManager {
	public Gson gson;
	public HashMap<String, RESTQueue> queues;
	public DiscLoader loader;

	public RESTManager(DiscLoader loader) {
		this.loader = loader;

		this.gson = new Gson();

		this.queues = new HashMap<String, RESTQueue>();
	}

	public void handleQueue(String route) {
		this.queues.get(route).handle();
	}

	public CompletableFuture<String> makeRequest(String url, int method, boolean auth, Object data) {
		APIRequest request = new APIRequest(url, method, auth, data);
		CompletableFuture<String> future = new CompletableFuture<String>();
		if (!this.queues.containsKey(url)) {
			this.queues.put(url, new RESTQueue(this));
		}

		request.setFuture(future);
		this.queues.get(url).addToQueue(request);
		this.handleQueue(url);
		return future;
	}

	public CompletableFuture<String> makeRequest(String url, int method, boolean auth) {
		return this.makeRequest(url, method, auth, null);
	}

	public CompletableFuture<Message> sendMessage(ITextChannel channel, String content, RichEmbed embed, Attachment attachment, File file) {
		if (content.length() < 1 && (embed == null && attachment == null))
			return null;
		CompletableFuture<Message> msgSent = new CompletableFuture<Message>();
		this.makeRequest(Constants.Endpoints.messages(channel.getID()), Constants.Methods.POST, true, new SendableMessage(content, embed, attachment, file)).thenAcceptAsync(action -> {
					msgSent.complete(new Message(channel, this.gson.fromJson(action, MessageJSON.class)));
				});
		return msgSent;
	}

	public CompletableFuture<Message> editMessage(ITextChannel channel, Message message, String content, RichEmbed embed, Attachment attachment, File file) {
		if (content.length() < 1 && (embed == null || attachment == null))
			return null;
		CompletableFuture<Message> future = new CompletableFuture<Message>();
		this.makeRequest(Constants.Endpoints.message(channel.getID(), message.id), Constants.Methods.PATCH, true,
				new SendableMessage(content, embed, attachment, file)).thenAcceptAsync(action -> {
					future.complete(new Message(channel, this.gson.fromJson(action, MessageJSON.class)));
				});
		return future;
	}

	public CompletableFuture<Message> deleteMessage(ITextChannel channel, Message message) {
		CompletableFuture<Message> future = new CompletableFuture<Message>();
		this.makeRequest(Constants.Endpoints.message(channel.getID(), message.id), Constants.Methods.DELETE, true)
				.thenAcceptAsync(action -> {
					future.complete(message);
				});
		return future;
	}

	public CompletableFuture<User> setUsername(String username) {
		CompletableFuture<User> future = new CompletableFuture<User>();
		this.makeRequest(Constants.Endpoints.currentUser, Constants.Methods.PATCH, true,
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
			this.makeRequest(Constants.Endpoints.currentUser, Constants.Methods.PATCH, true,
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
		String endpoint = member.id == this.loader.user.id ? Constants.Endpoints.guildNick(member.guild.id)
				: Constants.Endpoints.guildMember(member.guild.id, member.id);
		this.makeRequest(endpoint, Constants.Methods.PATCH, true, new JSONObject().put("nick", nick))
				.thenAcceptAsync(action -> {
					member.nick = nick;
					future.complete(member);
				});
		return future;
	}

	public CompletableFuture<Guild> modifyGuild(Guild guild, JSONObject data) {
		CompletableFuture<Guild> future = new CompletableFuture<Guild>();
		this.makeRequest(Constants.Endpoints.guild(guild.id), Constants.Methods.PATCH, true, data)
				.thenAcceptAsync(action -> {
					guild.setup(this.gson.fromJson(action, GuildJSON.class));
					future.complete(guild);
				});
		return future;
	}

	public CompletableFuture<GuildMember> loadGuildMember(Guild guild, String memberID) {
		CompletableFuture<GuildMember> future = new CompletableFuture<GuildMember>();
		this.makeRequest(Constants.Endpoints.guildMember(guild.id, memberID), Constants.Methods.GET, true)
				.thenAcceptAsync(action -> {
					future.complete(guild.addMember(this.gson.fromJson(action, MemberJSON.class)));
				});
		return future;
	}
	
	public CompletableFuture<VoiceChannel> createVoiceChannel(Guild guild, JSONObject data) {
		CompletableFuture<VoiceChannel> future = new CompletableFuture<VoiceChannel>();
		this.makeRequest(Constants.Endpoints.guildChannels(guild.id), Constants.Methods.POST, true, data).thenAcceptAsync(action -> {
			future.complete((VoiceChannel) this.loader.addChannel(this.gson.fromJson(action,  ChannelJSON.class), guild));
		});
		return future;
	}

	public CompletableFuture<OAuth2Application> getApplicationInfo() {
		CompletableFuture<OAuth2Application> future = new CompletableFuture<OAuth2Application>();
		this.makeRequest(Constants.Endpoints.currentOAuthApplication, Constants.Methods.GET, true).thenAcceptAsync(data -> {
			OAuthApplicationJSON appData = this.gson.fromJson(data, OAuthApplicationJSON.class);
			User owner = this.loader.addUser(appData.owner);
			future.complete(new OAuth2Application(appData, owner));
		});
		
		return future;
	}
	
}
