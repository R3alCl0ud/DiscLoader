package io.discloader.discloader.entity.channels;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.RichEmbed;
import io.discloader.discloader.entity.impl.ITextChannel;
import io.discloader.discloader.entity.message.Message;
import io.discloader.discloader.entity.message.MessageFetchOptions;
import io.discloader.discloader.entity.sendable.Attachment;
import io.discloader.discloader.entity.user.User;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.rest.actions.RESTFetchMessage;
import io.discloader.discloader.network.rest.actions.RESTFetchMessages;
import io.discloader.discloader.util.DLUtil.ChannelType;

/**
 * Represents a DM Channel on discord
 * 
 * @author Perry Berman
 */
public class PrivateChannel extends Channel implements ITextChannel {

    private HashMap<String, Message> messages;

    /**
     * The user that this DM Channel was opened with
     * 
     * @author Perry Berman
     */
    public User recipient;

    public PrivateChannel(DiscLoader loader, ChannelJSON data) {
        super(loader, data);
        this.type = ChannelType.DM;
        this.messages = new HashMap<>();
    }

    @Override
    public CompletableFuture<Message> fetchMessage(String id) {
        return new RESTFetchMessage(this, id).execute();
    }

    @Override
    public CompletableFuture<HashMap<String, Message>> fetchMessages() {
        return fetchMessages(new MessageFetchOptions());
    }

    @Override
    public CompletableFuture<HashMap<String, Message>> fetchMessages(MessageFetchOptions options) {
        return new RESTFetchMessages(this, options).execute();
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
    public CompletableFuture<Message> pinMessage(Message message) {
        return null;
    }

    public CompletableFuture<Message> sendEmbed(RichEmbed embed) {
        File file = null;
        Attachment attachment = null;
        if (embed.thumbnail != null && embed.thumbnail.file != null) {
            file = embed.thumbnail.file;
            embed.thumbnail.file = null;
            attachment = new Attachment(file.getName());
        }
        return this.loader.rest.sendMessage(this, null, embed, attachment, file);
    }

    public CompletableFuture<Message> sendMessage(String content) {
        return this.loader.rest.sendMessage(this, content, null, null, null);
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
        if (data.recipients[0] != null) {
            recipient = loader.users.get(data.recipients[0].id);
            if (recipient == null) {
                recipient = loader.addUser(data.recipients[0]);
            }
        }
    }

    @Override
    public CompletableFuture<Message> unpinMessage(Message message) {
        return null;
    }

}