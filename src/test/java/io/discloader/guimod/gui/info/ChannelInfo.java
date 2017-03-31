package io.discloader.guimod.gui.info;

import javax.swing.JLabel;

import io.discloader.discloader.core.entity.channel.Channel;
import io.discloader.discloader.core.entity.channel.GuildChannel;
import io.discloader.discloader.core.entity.channel.PrivateChannel;
import io.discloader.discloader.core.entity.channel.TextChannel;
import io.discloader.discloader.core.entity.channel.VoiceChannel;

public class ChannelInfo<T extends Channel> extends AbstractInfo<T> {
	
	private static final long serialVersionUID = -1685574314217673112L;
	
	public ChannelInfo() {
		
	}
	
	@Override
	public void update(Channel channel) {
		removeAll();
		revalidate();
		repaint();
		add(new JLabel(String.format("ID: %s", channel.getID())));
		if (channel instanceof GuildChannel) {
			GuildChannel gc = (GuildChannel) channel;
			add(new JLabel(String.format("Name: %s", gc.getName())));
			if (channel instanceof TextChannel) {
				TextChannel tc = (TextChannel) channel;
				add(new JLabel(String.format("Topic: %s", tc.getTopic())));
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
