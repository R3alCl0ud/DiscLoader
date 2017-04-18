package io.discloader.guimod.gui.list;

import javax.swing.DefaultListModel;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.registry.EntityRegistry;
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
		for (IChannel c : EntityRegistry.getChannels()) {
			switch (c.getType()) {
			case TEXT:
				GuildChannel gc = (GuildChannel) c;
				listModel.addElement(String.format("<html>%d<br>%s<br>Guild: %s</html>", gc.getID(), gc.getName(), gc.getGuild().getID()));
				items.add((T) c);
				break;
			case VOICE:
				GuildChannel ggc = (GuildChannel) c;
				listModel.addElement(String.format("<html>%d<br>%s<br>Guild: %s</html>", ggc.getID(), ggc.getName(), ggc.getGuild().getID()));
				items.add((T) c);
				break;
			case DM:
				PrivateChannel pc = (PrivateChannel) c;
				listModel.addElement(String.format("<html>%d<br>%s#%s<br>DM</html>", pc.getID(), pc.getRecipient().toString(), pc.getRecipient().getDiscriminator()));
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
