package io.discloader.guimod.gui.list;

import javax.swing.DefaultListModel;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.channels.Channel;
import io.discloader.discloader.entity.channels.GuildChannel;
import io.discloader.discloader.entity.channels.PrivateChannel;

public class ChannelList<T extends Channel> extends AbstractList<T> {

	private static final long serialVersionUID = 3073878318070310807L;

	public ChannelList(DiscLoader loader) {
		super(loader);
	}

	public DefaultListModel<Object> createListModel() {
		DefaultListModel<Object> listModel = new DefaultListModel<>();
		for (Channel c : loader.channels.values()) {
			switch (c.getType()) {
			case TEXT:
			case VOICE:
				GuildChannel gc = (GuildChannel) c;
				listModel.addElement(String.format("<html>%s<br>%s<br>Guild: %s</html>", gc.id, gc.name, gc.guild.id));
				items.add((T) c);
				break;
			case DM:
				PrivateChannel pc = (PrivateChannel) c;
				listModel.addElement(String.format("<html>%s<br>%s#%s<br>DM</html>", pc.id, pc.recipient.username, pc.recipient.discriminator));
				items.add((T) c);
				break;
			case GROUPDM:
				break;
			default:
				break;
			}
		}
		return listModel;
	}

}
