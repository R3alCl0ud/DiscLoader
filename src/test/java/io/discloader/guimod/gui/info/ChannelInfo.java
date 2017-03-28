package io.discloader.guimod.gui.info;

import javax.swing.JLabel;

import io.discloader.discloader.entity.channels.impl.Channel;
import io.discloader.discloader.entity.channels.impl.GuildChannel;
import io.discloader.discloader.entity.channels.impl.PrivateChannel;
import io.discloader.discloader.entity.channels.impl.TextChannel;
import io.discloader.discloader.entity.channels.impl.VoiceChannel;

public class ChannelInfo<T extends Channel> extends AbstractInfo<T> {

	private static final long serialVersionUID = -1685574314217673112L;

	public ChannelInfo() {

	}

	public void update(Channel channel) {
		removeAll();
		revalidate();
		repaint();
		add(new JLabel(String.format("ID: %s", channel.getID())));
		if (channel instanceof GuildChannel) {
			GuildChannel gc = (GuildChannel) channel;
			add(new JLabel(String.format("Name: %s", gc.name)));
			if (channel instanceof TextChannel) {
				TextChannel tc = (TextChannel) channel;
				add(new JLabel(String.format("Topic: %s", tc.topic)));
			} else if (channel instanceof VoiceChannel) {
				VoiceChannel vc = (VoiceChannel) channel;
				add(new JLabel(String.format("Bitrate: %d", vc.bitrate)));
			}
		} else if (channel instanceof PrivateChannel) {

		}
		add(new JLabel(String.format("Type: %s", channel.getType())));
		revalidate();
		repaint();
	}

}
