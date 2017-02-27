package io.discloader.guimod.gui.tab;

import javax.swing.event.ListSelectionEvent;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.discovery.ModContainer;
import io.discloader.guimod.gui.info.ModInfo;
import io.discloader.guimod.gui.list.ModList;

public class ModsFolder<T extends ModContainer> extends AbstractFolder<T, ModList<T>, ModInfo<T>> {

	private static final long serialVersionUID = -920545644404939296L;

	public ModsFolder(DiscLoader loader) {
		super(loader);
	}
	
	@Override
	public ModList<T> createList() {
		return new ModList<T>(this.loader);
	}
	
	@Override
	public ModInfo<T> createInfo() {
		return new ModInfo<T>();
	}
	

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (this.list.folders.getSelectedIndex() != -1) {
			this.info.update(this.list.items.get(list.folders.getSelectedIndex()));
		}
	}
}
