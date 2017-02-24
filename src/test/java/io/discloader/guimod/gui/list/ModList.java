package io.discloader.guimod.gui.list;

import javax.swing.DefaultListModel;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.discovery.ModContainer;
import io.discloader.discloader.common.registry.ModRegistry;

public class ModList<T> extends AbstractList<T> {

	private static final long serialVersionUID = -1567206350246898955L;

	public ModList(DiscLoader loader) {
		super(loader);
	}

	@SuppressWarnings("unchecked")
	@Override
	public DefaultListModel<Object> createListModel() {
		DefaultListModel<Object> listModel = new DefaultListModel<Object>();
		for (ModContainer mc : ModRegistry.mods.values()) {
			listModel.addElement(String.format("<html>%s<br>%s<br>%s</html>", mc.modInfo.name(), mc.modInfo.version(),
					mc.modInfo.desc()));
			this.items.add((T) mc);
		}
		return listModel;
	}
}
