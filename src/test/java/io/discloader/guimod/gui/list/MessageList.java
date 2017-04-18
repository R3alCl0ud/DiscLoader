package io.discloader.guimod.gui.list;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.message.IMessage;

public class MessageList extends JPanel {

	private static final long serialVersionUID = -3043115520940493712L;
	public ITextChannel tchannel;
	public final JList<Object> folders;
	public final ArrayList<IMessage> items;

	public MessageList(ITextChannel channel) {
		super();
		tchannel = channel;
		this.items = new ArrayList<>();
		JScrollPane pane = new JScrollPane();
		JScrollBar bar = new JScrollBar();
		bar.setUI(new ScrollBar());
		bar.setBackground(new Color(0x23272A));
		bar.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 2));
		bar.setForeground(new Color(0xFFFFFF));
		bar.putClientProperty("JScrollBar.fastWheelScrolling", true);
		pane.setVerticalScrollBar(bar);
		pane.setBorder(BorderFactory.createLineBorder(new Color(0x99AAB5), 5));
		this.setBackground(new Color(0, 0, 0, 0));
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.folders = new JList<Object>(this.createListModel());
		this.folders.setBackground(new Color(0x23272A));
		this.folders.setCellRenderer(new CellRenderer());
		this.folders.setFixedCellHeight(60);
		this.folders.setFixedCellWidth(220);
		this.folders.setBorder(BorderFactory.createEmptyBorder());
		this.add(Box.createRigidArea(new Dimension(0, 40)));
		this.add(pane);
		this.add(Box.createRigidArea(new Dimension(0, 40)));
		pane.setViewportView(this.folders);
		this.folders.setMinimumSize(new Dimension(300, 400));
		setMinimumSize(new Dimension(300, 500));
		this.setMaximumSize(new Dimension(300, 1000));
		if (tchannel.getMessageCollection().size() < 50) fetchMessages();
	}

	public DefaultListModel<Object> createListModel() {
		DefaultListModel<Object> list = new DefaultListModel<>();
		if (tchannel != null) {
			for (IMessage message : tchannel.getMessageCollection()) {
				list.addElement(format(message.getID(), message.getAuthor().toString()));
				items.add(message);
			}
		}
		return list;
	}

	public String format(long id, String author) {
		return String.format("<html>%d<br>%s</html>", id, author);
	}

	public void fetchMessages() {
		tchannel.fetchMessages().thenAcceptAsync(messages -> {
			DefaultListModel<Object> list = ((DefaultListModel<Object>) folders.getModel());
			list.clear();
			for (IMessage message : messages.values()) {
				list.addElement(format(message.getID(), message.getAuthor().toString()));
				items.add(message);
			}
		});
	}

}
