package io.discloader.guimod.gui.info;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;

import io.discloader.discloader.core.entity.channel.Channel;
import io.discloader.discloader.core.entity.channel.GuildChannel;
import io.discloader.discloader.core.entity.channel.PrivateChannel;
import io.discloader.discloader.core.entity.channel.TextChannel;
import io.discloader.discloader.core.entity.channel.VoiceChannel;
import io.discloader.discloader.entity.channel.IPrivateChannel;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.guimod.GUIMod;
import io.discloader.guimod.gui.TabbedPanel;
import io.discloader.guimod.gui.tab.MessagesTab;

public class ChannelInfo<T extends Channel> extends AbstractInfo<T> implements ActionListener {
	
	private static final long serialVersionUID = -1685574314217673112L;
	
	private boolean messages = false;
	private ITextChannel textChannel;
	
	public ChannelInfo() {
		
	}
	
	@Override
	public void update(Channel channel) {
		removeAll();
		revalidate();
		repaint();
		add(Box.createRigidArea(new Dimension(0, 40)));
		add(new JLabel(String.format("ID: %s", channel.getID())));
		if (channel instanceof GuildChannel) {
			GuildChannel gc = (GuildChannel) channel;
			add(new JLabel(String.format("Name: %s", gc.getName())));
			if (channel instanceof TextChannel) {
				TextChannel tc = (TextChannel) channel;
				textChannel = tc;
				add(new JLabel(format("Topic", tc.getTopic() == null ? "" : tc.getTopic())));
				JButton open = new JButton("View Messages");
				open.addActionListener(this);
				open.setActionCommand("OPEN");
				add(open);
			} else if (channel instanceof VoiceChannel) {
				VoiceChannel vc = (VoiceChannel) channel;
				textChannel = null;
				add(new JLabel(String.format("Bitrate: %d", vc.bitrate)));
			}
		} else if (channel instanceof PrivateChannel) {
			IPrivateChannel pc = (IPrivateChannel) channel;
			textChannel = pc;
		}
		add(new JLabel(String.format("Type: %s", channel.getType())));
		add(new Box.Filler(new Dimension(0, 300), new Dimension(0, 400), new Dimension(0, (int) GUIMod.screenSize.getHeight())));
		revalidate();
		repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("CLOSE") && messages) {
			messages = false;
		} else if (e.getActionCommand().equals("OPEN") && !messages && textChannel != null) {
			messages = true;
			try {
				TabbedPanel.addTab("Messages", new MessagesTab(textChannel, this));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
}
