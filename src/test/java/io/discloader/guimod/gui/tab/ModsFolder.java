package io.discloader.guimod.gui.tab;

import javax.swing.event.ListSelectionEvent;

import io.discloader.discloader.client.render.list.AbstractList;
import io.discloader.discloader.client.render.list.ModList;
import io.discloader.discloader.client.render.panel.info.AbstractInfo;
import io.discloader.discloader.client.render.panel.info.ModInfo;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.discovery.ModContainer;

public class ModsFolder<T extends ModContainer> extends AbstractFolder<T> {

	private static final long serialVersionUID = -920545644404939296L;

	public ModsFolder(DiscLoader loader) {
		super(loader);
	}
	
	@Override
	public AbstractList<T> createList() {
		return new ModList<T>(this.loader);
	}
	
	@Override
	public AbstractInfo<T> createInfo() {
		return new ModInfo<T>();
	}
	

	@Override
	public void valueChanged(ListSelectionEvent e) {
		System.out.println("Test");
		if (this.list.folders.getSelectedIndex() != -1) {
			this.info.update(this.list.items.get(list.folders.getSelectedIndex()));
		}
	}
}
