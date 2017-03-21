package io.discloader.discloader.entity.channels;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.impl.IChannel;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.util.DLUtil.ChannelType;

public class Channel implements IChannel {

    /**
     * The channel's Snowflake ID.
     */
    public String id;

    protected ChannelType type;

    /**
     * The current instance of DiscLoader
     */
    public final DiscLoader loader;

    public Channel(DiscLoader loader, ChannelJSON data) {
        this.loader = loader;

        this.type = ChannelType.CHANNEL;

        if (data != null)
            this.setup(data);
    }

    @Override
    public String getID() {
        return this.id;
    }

    @Override
    public ChannelType getType() {
        return this.type;
    }

    @Override
    public boolean isPrivate() {
        return this.type != ChannelType.TEXT && this.type != ChannelType.VOICE;
    }

    public void setup(ChannelJSON data) {
        this.id = data.id;
    }

    public String toMention() {
        return String.format("<#%s>", id);
    }

    @Override
    public DiscLoader getLoader() {
        return loader;
    }
}
