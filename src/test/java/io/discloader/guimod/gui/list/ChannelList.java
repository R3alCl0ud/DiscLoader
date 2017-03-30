package io.discloader.guimod.gui.list;

import javax.swing.DefaultListModel;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.core.entity.channel.Channel;
import io.discloader.discloader.core.entity.channel.GuildChannel;
import io.discloader.discloader.core.entity.channel.PrivateChannel;
import io.discloader.discloader.entity.channel.IChannel;

public class ChannelList<T extends Channel> extends AbstractList<T> {

	private static final long serialVersionUID = 3073878318070310807L;

	public ChannelList(DiscLoader loader) {
		super(loader);
	}

	@Override
	@SuppressWarnings("unchecked")
	public DefaultListModel<Object> createListModel() {
		DefaultListModel<Object> listModel = new DefaultListModel<>();
		for (IChannel c : loader.channels.values()) {
			switch (c.getType()) {
			case TEXT:
				GuildChannel gc = (GuildChannel) c;
					listModel.addElement(String.format("<html>%s<br>%s<br>Guild: %s</html>", gc.getID(), gc.name, gc.getGuild().getID()));
				items.add((T) c);
				break;
			case VOICE:
				GuildChannel ggc = (GuildChannel) c;
					listModel.addElement(String.format("<html>%s<br>%s<br>Guild: %s</html>", ggc.getID(), ggc.name, ggc.getGuild().getID()));
				items.add((T) c);
				break;
			case DM:
				PrivateChannel pc = (PrivateChannel) c;
					listModel.addElement(String.format("<html>%s<br>%s#%s<br>DM</html>", pc.getID(), pc.getRecipient().getUsername(), pc.getRecipient().getDiscriminator()));
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
