package io.discloader.guimod.gui.tab;

import javax.swing.event.ListSelectionEvent;

import io.discloader.guimod.gui.list.AbstractList;
import io.discloader.guimod.gui.list.UserList;
import io.discloader.discloader.client.render.panel.info.AbstractInfo;
import io.discloader.discloader.client.render.panel.info.UserInfo;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.User;

public class UserFolders<T extends User> extends AbstractFolder<T> {

	private static final long serialVersionUID = 3191743262883151288L;

	public UserFolders(DiscLoader loader) {
		super(loader);
	}

	@Override
	public AbstractList<T> createList() {
		return new UserList<T>(this.loader);
	}

	@Override
	public AbstractInfo<T> createInfo() {
		return new UserInfo<T>();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (list.folders.getSelectedIndex() != -1) {
			info.update(list.items.get(list.folders.getSelectedIndex()));
		}
	}
}
