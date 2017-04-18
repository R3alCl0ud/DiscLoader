package io.discloader.guimod.gui.tab;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.guimod.gui.TabbedPanel;
import io.discloader.guimod.gui.info.ChannelInfo;
import io.discloader.guimod.gui.info.MessageInfo;
import io.discloader.guimod.gui.list.MessageList;

public class MessagesTab extends JPanel implements ActionListener, ListSelectionListener {

	private static final long serialVersionUID = -239549662593236507L;
	private ITextChannel tchannel;
	private JButton close;
	public MessageInfo info;
	public MessageList list;

	public MessagesTab(ITextChannel channel, ChannelInfo<?> parent) {
		super();
		tchannel = channel;

		this.list = this.createList();
		this.setBackground(new Color(0x2C2F33));
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.list.folders.addListSelectionListener(this);
		this.add(Box.createRigidArea(new Dimension(50, 0)));
		this.add(this.list);
		this.add(Box.createRigidArea(new Dimension(50, 0)));
		close = new JButton("Close");
		close.addActionListener(this);
		close.addActionListener(parent);
		close.setActionCommand("CLOSE");
		add(close);
		this.add(this.info = this.createInfo());
	}

	public MessageList createList() {
		return new MessageList(tchannel);
	}

	public MessageInfo createInfo() {
		return new MessageInfo();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("CLOSE")) {
			TabbedPanel.removeTab(this);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (list.folders.getSelectedIndex() != -1) {
			info.update(list.items.get(list.folders.getSelectedIndex()));
		}
	}

}
