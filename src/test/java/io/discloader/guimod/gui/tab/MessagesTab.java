package io.discloader.guimod.gui.tab;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.guimod.gui.TabbedPanel;
import io.discloader.guimod.gui.info.ChannelInfo;
import io.discloader.guimod.gui.info.MessageInfo;
import io.discloader.guimod.gui.list.MessageList;

public class MessagesTab extends AbstractTab<IMessage, MessageList, MessageInfo> implements ActionListener {

	private static final long serialVersionUID = -239549662593236507L;
	private static ITextChannel tchannel;
	private JButton close;

	public MessagesTab(ITextChannel channel, ChannelInfo<?> parent) {
		super((tchannel = channel) != null ? tchannel.getLoader() : DiscLoader.getDiscLoader());
		// tchannel = channel;
		close = new JButton("Close");
		close.addActionListener(this);
		close.addActionListener(parent);
		close.setActionCommand("CLOSE");
	}

	private MessagesTab(DiscLoader loader) {
		super(loader);
	}

	@Override
	public MessageList createList() {
		System.out.println(tchannel != null);// && tchannel.getLoader() != null
		return new MessageList(tchannel);
	}

	@Override
	public MessageInfo createInfo() {
		return new MessageInfo();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("CLOSE")) {
			TabbedPanel.removeTab(this);
		}
	}
}
