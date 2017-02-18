/**
 * 
 */
package io.discloader.discloader.client.renderer.panel;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import io.discloader.discloader.client.renderer.texture.GuildIcon;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.structures.Guild;

/**
 * @author Perry Berman
 *
 */
public class FolderPanel extends JPanel {

	private static final long serialVersionUID = 5203896810516469678L;
	private CenterPanel folders;

	private JScrollPane basePane;

	private DiscLoader loader;

	public FolderPanel(DiscLoader loader) {
		super();
		this.loader = loader;
		this.basePane = new JScrollPane();
		for (GuildIcon icon : this.loader.clientRegistry.textureRegistry.getGuildIcons().values()) {
			System.out.println("test");
			Guild guild = icon.guild;
			System.out.println(icon != null);
			JLabel guildIcon = new JLabel(guild.id);
			guildIcon.setIcon(icon.getImageIcon());
			this.basePane.add(guildIcon);
		}

		this.folders = new CenterPanel(this.basePane);
		this.validate();
	}

}
