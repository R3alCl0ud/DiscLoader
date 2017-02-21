package io.discloader.discloader.client.renderer.panel;

import java.awt.GridLayout;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import io.discloader.discloader.client.renderer.panel.folders.GuildFolders;
import io.discloader.discloader.client.renderer.panel.folders.UserFolders;
import io.discloader.discloader.common.DiscLoader;

public class TabbedPanel extends JPanel {

	private static final long serialVersionUID = -6974630780146823721L;
	public final DiscLoader loader;

	public TabbedPanel(DiscLoader loader) {
		super(new GridLayout(1, 1));
		this.loader = loader;
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Users", createImageIcon("texture.gui.icons.missing-icon", "Missing Icon"),
				new UserFolders(this.loader), "Users");
		tabbedPane.addTab("Guilds", createImageIcon("texture.gui.icons.missing-icon", "Missing Icon"),
				new GuildFolders(this.loader), "Guilds");

		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		this.add(tabbedPane);
		this.validate();
	}

	protected ImageIcon createImageIcon(String path, String description) {
		URL imgURL = ClassLoader.getSystemResource(String.format("assets/discloader/%s.png", path.replace('.', '/')));
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

}
