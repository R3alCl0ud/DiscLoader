package io.discloader.discloader.entity.channels;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.RichEmbed;
import io.discloader.discloader.entity.guild.Guild;
import io.discloader.discloader.entity.guild.GuildMember;
import io.discloader.discloader.entity.impl.ITextChannel;
import io.discloader.discloader.entity.message.Message;
import io.discloader.discloader.entity.message.MessageFetchOptions;
import io.discloader.discloader.entity.sendable.Attachment;
import io.discloader.discloader.entity.user.User;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.rest.actions.BulkDelete;
import io.discloader.discloader.network.rest.actions.FetchMessage;
import io.discloader.discloader.network.rest.actions.FetchMessages;
import io.discloader.discloader.network.rest.actions.PinMessage;
import io.discloader.discloader.network.rest.actions.PinnedMessages;
import io.discloader.discloader.network.rest.actions.StartTyping;
import io.discloader.discloader.network.rest.actions.UnpinMessage;
import io.discloader.discloader.util.DLUtil.ChannelType;

/**
 * Represents a TextChannel in a Guild on Discord
 * 
 * 
 * @author Perry Berman
 * @since 0.0.1
 * @version 3
 */
public class TextChannel extends GuildChannel implements ITextChannel {

    /**
     * A {@link HashMap} of the channel's cached messages. Indexed by {@link Message#id}.
     * 
     * @author Perry Berman
     * @since 0.0.1
     */
    private final HashMap<String, Message> messages;

    private HashMap<String, User> typing;

    /**
     * The channel's topic
     * 
     * @author Perry Berman
     * @since 0.0.3
     */
    public String topic;

    public TextChannel(Guild guild, ChannelJSON data) {
        super(guild, data);

        messages = new HashMap<>();
        typing = new HashMap<>();
    }

    @Override
    public CompletableFuture<HashMap<String, Message>> deleteMessages(HashMap<String, Message> messages) {
        return new BulkDelete(this, messages).execute();
    }

    @Override
    public CompletableFuture<HashMap<String, Message>> deleteMessages(Message... messages) {
        HashMap<String, Message> msgs = new HashMap<>();
        for (Message message : messages) {
            msgs.put(message.id, message);
        }
        return deleteMessages(msgs);
    }

    @Override
    public CompletableFuture<Message> fetchMessage(String id) {
        return new FetchMessage(this, id).execute();
    }

    @Override
    public CompletableFuture<HashMap<String, Message>> fetchMessages() {
        return fetchMessages(new MessageFetchOptions());
    }

    @Override
    public CompletableFuture<HashMap<String, Message>> fetchMessages(MessageFetchOptions options) {
        return new FetchMessages(this, options).execute();
    }

    @Override
    public Message getMessage(String id) {
        return this.messages.get(id);
    }

    @Override
    public HashMap<String, Message> getMessages() {
        return this.messages;
    }

    @Override
    public CompletableFuture<HashMap<String, Message>> getPinnedMessages() {
        return new PinnedMessages(this).execute();
    }

    @Override
    public HashMap<String, User> getTyping() {
        return typing;
    }

    /**
     * Checks if a certain {@link GuildMember guild member} is typing in this channel
     * 
     * @param member The member to check.
     * @return {@code true} if the member is typing, false otherwise.
     */
    public boolean isTyping(GuildMember member) {
        return typing.containsKey(member.id);
    }

    @Override
    public boolean isTyping(User user) {
        return typing.containsKey(user.id);
    }

    @Override
    public CompletableFuture<Message> pinMessage(Message message) {
        return new PinMessage(message).execute();
    }

    public CompletableFuture<Message> sendEmbed(RichEmbed embed) {
        File file = null;
        Attachment attachment = null;
        if (embed.thumbnail != null && embed.thumbnail.file != null) {
            file = embed.thumbnail.file;
            embed.thumbnail.file = null;
            attachment = new Attachment(file.getName());
        }
        return loader.rest.sendMessage(this, null, embed, attachment, file);
    }

    public CompletableFuture<Message> sendMessage(String content) {
        return loader.rest.sendMessage(this, content, null, null, null);
    }

    public CompletableFuture<Message> sendMessage(String content, RichEmbed embed) {
        File file = null;
        Attachment attachment = null;
        if (embed.thumbnail != null && embed.thumbnail.file != null) {
            file = embed.thumbnail.file;
            embed.thumbnail.file = null;
            attachment = new Attachment(file.getName());
        }
        return this.loader.rest.sendMessage(this, content, embed, attachment, file);
    }

    public void setup(ChannelJSON data) {
        super.setup(data);

        this.type = ChannelType.TEXT;

        this.topic = data.topic;
    }

    @Override
    public CompletableFuture<HashMap<String, User>> startTyping() {
        return new StartTyping(this).execute();
    }

    @Override
    public CompletableFuture<Message> unpinMessage(Message message) {
        return new UnpinMessage(message).execute();
    }

}
