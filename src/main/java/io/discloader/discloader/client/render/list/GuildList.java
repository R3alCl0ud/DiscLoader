package io.discloader.discloader.client.render.list;

import javax.swing.DefaultListModel;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.Guild;

public class GuildList<T extends Guild> extends AbstractList<T> {
	private static final long serialVersionUID = -1589736360078814907L;

	public GuildList(DiscLoader loader) {
		super(loader);
		DefaultListModel<Object> listModel = new DefaultListModel<Object>();
		for (Guild guild : this.loader.guilds.values()) {
			listModel.addElement(String.format("<html><font color=#748b9a>name:</font> %s<br><font color=#748b9a>id:</font> %s</html>", guild.name, guild.id));
		}

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public DefaultListModel<Object> createListModel() {
		DefaultListModel<Object> listModel = new DefaultListModel<Object>();
		for (Guild guild : this.loader.guilds.values()) {
			listModel.addElement(String.format("<html><font color=#748b9a>name:</font> %s<br><font color=#748b9a>id:</font> %s</html>", guild.name, guild.id));
			this.items.add((T) guild);
		}
		return listModel;
	}

}
