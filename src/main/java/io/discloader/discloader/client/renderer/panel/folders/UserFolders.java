package io.discloader.discloader.client.renderer.panel.folders;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import io.discloader.discloader.client.renderer.list.UserList;
import io.discloader.discloader.client.renderer.panel.info.UserInfo;
import io.discloader.discloader.common.DiscLoader;

public class UserFolders extends JPanel {

	private static final long serialVersionUID = 3191743262883151288L;
	public final DiscLoader loader;
	public final UserList list;
	public final UserInfo info;

	public UserFolders(DiscLoader loader) {
		this.loader = loader;
		this.setBackground(new Color(0x2C2F33));
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.list = new UserList(this.loader);
		this.add(Box.createRigidArea(new Dimension(50, 0)));
		this.add(this.list);
		this.add(Box.createRigidArea(new Dimension(50, 0)));
		this.add(this.info = new UserInfo());
	}

}
