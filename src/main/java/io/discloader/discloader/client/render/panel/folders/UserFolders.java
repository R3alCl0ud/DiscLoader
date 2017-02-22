package io.discloader.discloader.client.render.panel.folders;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import io.discloader.discloader.client.render.list.UserList;
import io.discloader.discloader.client.render.panel.info.UserInfo;
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
		this.list.folders.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (list.folders.getSelectedIndex() != -1) {
					info.update(list.users.get(list.folders.getSelectedIndex()));
				}
			}

		});
		this.add(Box.createRigidArea(new Dimension(50, 0)));
		this.add(this.list);
		this.add(Box.createRigidArea(new Dimension(50, 0)));
		this.add(this.info = new UserInfo());
	}

}
