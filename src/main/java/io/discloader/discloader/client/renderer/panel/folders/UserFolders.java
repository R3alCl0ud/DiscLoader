package io.discloader.discloader.client.renderer.panel.folders;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import io.discloader.discloader.client.renderer.list.UserList;
import io.discloader.discloader.common.DiscLoader;

public class UserFolders extends JPanel {

	private static final long serialVersionUID = 3191743262883151288L;
	public final DiscLoader loader;
	public final UserList list;

	public UserFolders(DiscLoader loader) {
		super();
		this.loader = loader;	
		this.list = new UserList(this.loader);

		JSplitPane pane = new JSplitPane();
		pane.setLeftComponent(this.list);
		pane.setMinimumSize(new Dimension(550, 550));
		this.add(pane);

	}

}
