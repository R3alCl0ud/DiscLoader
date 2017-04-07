package io.discloader.guimod.gui.list;

import javax.swing.DefaultListModel;

import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.message.IMessage;

public class MessageList extends AbstractList<IMessage> {

	private static final long serialVersionUID = -3043115520940493712L;
	public ITextChannel tchannel;

	public MessageList(ITextChannel channel) {
		super(channel.getLoader());
		tchannel = channel;
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

}
