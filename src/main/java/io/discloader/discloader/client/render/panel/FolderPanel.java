/**
 * 
 */
package io.discloader.discloader.client.render.panel;

import io.discloader.discloader.client.registry.TextureRegistry;
import io.discloader.discloader.client.render.texture.icon.GuildIcon;
import io.discloader.discloader.common.DiscLoader;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * @author Perry Berman
 *
 */
public class FolderPanel extends JPanel {

	private static final long serialVersionUID = 5203896810516469678L;
	private CenterPanel folders;

	private JScrollPane basePane;

//	private DiscLoader loader;

	public FolderPanel(DiscLoader loader) {
//		super();
//		this.loader = loader;
		this.basePane = new JScrollPane();
		for (GuildIcon guildIcon : TextureRegistry.getGuildIcons().values()) {
			System.out.println("test");
			System.out.println(guildIcon != null);
			JLabel icon = new JLabel(guildIcon.getImageIcon());
			icon.setToolTipText("Test");
			this.add(icon);
		}
		this.basePane.setMinimumSize(new Dimension(200, 200));
		this.basePane.setSize(new Dimension(500, 500));
		this.basePane.validate();
		System.out.printf("width: %d, height: %d\n", this.basePane.getWidth(), this.basePane.getHeight());
		this.folders = new CenterPanel(this.basePane);
		this.folders.setSize(new Dimension(720, 400));
		this.folders.validate();
		this.add(this.folders);
//		this.setSize(new Dimension(720, 400));
		this.validate();
	}

}
