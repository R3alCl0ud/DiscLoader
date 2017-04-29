package io.discloader.guimod.gui.info;

import java.awt.Dimension;
import java.time.format.DateTimeFormatter;

import javax.swing.Box;
import javax.swing.JLabel;

import io.discloader.discloader.entity.message.IMessage;
import io.discloader.guimod.GUIMod;

public class MessageInfo extends AbstractInfo<IMessage> {
	
	private static final long serialVersionUID = -4309829696960633630L;
	
	private JLabel id;
	private JLabel content;
	private JLabel author;
	private JLabel timestamp;
	private JLabel edited;
	private JLabel type;
	private MentionInfo mentions;
	
	public MessageInfo() {
		super();
		add(Box.createRigidArea(new Dimension(0, 40)));
		add(id = new JLabel("ID: "));
		add(content = new JLabel("Content: "));
		add(author = new JLabel("Author: "));
		add(timestamp = new JLabel("Timestamp: "));
		add(edited = new JLabel("Edited: "));
		add(type = new JLabel("Type: "));
		add(mentions = new MentionInfo());
		add(new Box.Filler(new Dimension(0, 300), new Dimension(0, 400), new Dimension(0, (int) GUIMod.screenSize.getHeight())));
	}
	
	public void update(IMessage message) {
		id.setText(format("ID", message.getID()));
		content.setText(format("Content", message.getContent()));
		author.setText(format("Author", message.getAuthor()));
		timestamp.setText(format("Timestamp", message.createdAt().format(DateTimeFormatter.RFC_1123_DATE_TIME)));
		edited.setText(format("Edited", message.getEditedAt() == null ? "false" : message.getEditedAt().format(DateTimeFormatter.RFC_1123_DATE_TIME)));
		type.setText(format("Type", message.isSystem()));
		mentions.update(message.getMentions());
	}
	
	public String format(String place, Object data) {
		return String.format("<html>%s: %s</html>", place, data.toString());
	}
	
}
