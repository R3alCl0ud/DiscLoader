package io.discloader.discloader.network.rest.actions;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.channels.ITextChannel;
import io.discloader.discloader.entity.message.Message;
import io.discloader.discloader.util.DLUtil.Endpoints;
import io.discloader.discloader.util.DLUtil.Methods;

/**
 * @author Perry Berman
 *
 */
public class BulkDelete extends RESTAction<HashMap<String, Message>> {
    public ITextChannel channel;
    public HashMap<String, Message> messages;

    public BulkDelete(ITextChannel channel, HashMap<String, Message> messages) {
        super(channel.getLoader());
        this.channel = channel;
        this.messages = messages;
    }

    public CompletableFuture<HashMap<String, Message>> execute() {
        return super.execute(loader.rest.makeRequest(Endpoints.bulkDelete(channel.getID()), Methods.GET, true));
    }

    @Override
    public void complete(String s, Throwable ex) {
        if (ex != null) {
            future.completeExceptionally(ex);
            return;
        }
        future.complete(messages);
    }

}
