package io.discloader.discloader.client.render.panel;

import java.awt.GridLayout;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import io.discloader.discloader.client.render.panel.folders.GuildFolders;
import io.discloader.discloader.client.render.panel.folders.ModsFolder;
import io.discloader.discloader.client.render.panel.folders.UserFolders;
import io.discloader.discloader.common.DiscLoader;

public class TabbedPanel extends JPanel {

	private static final long serialVersionUID = -6974630780146823721L;
	public final DiscLoader loader;

	public TabbedPanel(DiscLoader loader) {
		super(new GridLayout(1, 1));
		this.loader = loader;
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Mods", new ModsFolder(this.loader));
		tabbedPane.addTab("Users", resizeImageIcon(createImageIcon("texture.gui.icons.missing-icon", "Missing Icon"), 16, 16),
				new UserFolders(this.loader), "Users");
		tabbedPane.addTab("Guilds", resizeImageIcon(createImageIcon("texture.gui.icons.missing-icon", "Missing Icon"), 16, 16),
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
	
	protected ImageIcon resizeImageIcon(ImageIcon imageIcon, int width, int height) {
		return new ImageIcon(imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
	}

}
