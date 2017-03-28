package io.discloader.guimod.gui.list;

import javax.swing.DefaultListModel;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.guild.Guild;

public class GuildList<T extends Guild> extends AbstractList<T> {
	private static final long serialVersionUID = -1589736360078814907L;

	public GuildList(DiscLoader loader) {
		super(loader);

	}

	@Override
	@SuppressWarnings("unchecked")
	public DefaultListModel<Object> createListModel() {
		DefaultListModel<Object> listModel = new DefaultListModel<Object>();
		for (Guild guild : this.loader.guilds.values()) {
			listModel.addElement(String.format("<html>%s<br>%s<br>%s</html>", guild.name, guild.id, guild.getVoiceRegion().id));
			this.items.add((T) guild);
		}
		return listModel;
	}

}
