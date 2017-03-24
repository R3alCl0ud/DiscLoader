package io.discloader.guimod.gui.tab;

import javax.swing.event.ListSelectionEvent;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.channels.impl.Channel;
import io.discloader.guimod.gui.info.ChannelInfo;
import io.discloader.guimod.gui.list.ChannelList;

public class ChannelTab<T extends Channel> extends AbstractTab<T, ChannelList<T>, ChannelInfo<T>> {

	private static final long serialVersionUID = -3615911759179504065L;

	public ChannelTab(DiscLoader loader) {
		super(loader);
	}

	public ChannelInfo<T> createInfo() {
		return new ChannelInfo<T>();
	}

	public ChannelList<T> createList() {
		return new ChannelList<T>(loader);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (this.list.folders.getSelectedIndex() != -1) {
			this.info.update(this.list.items.get(list.folders.getSelectedIndex()));
		}
	}
	
}
