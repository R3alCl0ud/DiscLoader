package io.discloader.discloader.client.renderer.list;

import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.structures.User;

public class UserList extends JScrollPane {
	private static final long serialVersionUID = -1589736360078814907L;
	public final DiscLoader loader;
	public final JList<Object> folders;

	public UserList(DiscLoader loader) {
		super();
		this.loader = loader;
		DefaultListModel<Object> listModel = new DefaultListModel<Object>();
		for (User user : this.loader.users.values()) {
			listModel.addElement(user.id);
		}
		this.folders = new JList<Object>(listModel);
		this.setViewportView(this.folders);
		this.setMinimumSize(new Dimension(240, 400));
		this.setMaximumSize(new Dimension(240, 1000));
	}

}
