package io.discloader.guimod.gui.list;

import javax.swing.DefaultListModel;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.user.IUser;

public class UserList<T> extends AbstractList<T> {
	private static final long serialVersionUID = -1589736360078814907L;
	
	public UserList(DiscLoader loader) {
		super(loader);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public DefaultListModel<Object> createListModel() {
		DefaultListModel<Object> listModel = new DefaultListModel<Object>();
		for (IUser user : this.loader.users.values()) {
			listModel.addElement(String.format("<html>%s#%s<br>%s</html>", user.getUsername(), user.getDiscriminator(), user.getID()));
			this.items.add((T) user);
		}
		return listModel;
	}
}
