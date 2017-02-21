package io.discloader.discloader.client.renderer.list;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.structures.User;

public class UserList extends JScrollPane {
	private static final long serialVersionUID = -1589736360078814907L;
	public final DiscLoader loader;
	private final JList folders;

	public UserList(DiscLoader loader) {
		super();
		this.loader = loader;
		DefaultListModel listModel = new DefaultListModel();
		for (User user : this.loader.users.values()) {
			listModel.addElement(user.id);
		}
		this.folders = new JList(listModel);
		this.setViewportView(this.folders);
		this.folders.setSize(500, 500);
//		this.validate();
	}

}
