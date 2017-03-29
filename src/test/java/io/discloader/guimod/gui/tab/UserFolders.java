package io.discloader.guimod.gui.tab;

import javax.swing.event.ListSelectionEvent;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.entity.user.User;
import io.discloader.guimod.gui.info.UserInfo;
import io.discloader.guimod.gui.list.UserList;

public class UserFolders<T extends User> extends AbstractTab<T, UserList<T>, UserInfo<T>> {

	private static final long serialVersionUID = 3191743262883151288L;

	public UserFolders(DiscLoader loader) {
		super(loader);
	}

	@Override
	public UserList<T> createList() {
		return new UserList<T>(this.loader);
	}

	@Override
	public UserInfo<T> createInfo() {
		return new UserInfo<T>();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (list.folders.getSelectedIndex() != -1) {
			info.update(list.items.get(list.folders.getSelectedIndex()));
		}
	}
}
