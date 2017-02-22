/**
 * 
 */
package io.discloader.discloader.client.renderer.panel.folders;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import io.discloader.discloader.client.renderer.list.GuildList;
import io.discloader.discloader.client.renderer.panel.info.GuildInfo;
import io.discloader.discloader.common.DiscLoader;

/**
 * @author Perry Berman
 *
 */
public class GuildFolders extends JPanel {

	private static final long serialVersionUID = -1589736360078814907L;
	public final DiscLoader loader;
	public final GuildList list;
	public final GuildInfo info;

	public GuildFolders(DiscLoader loader) {
		super(new GridLayout(1, 3));
		this.loader = loader;
		this.setBackground(new Color(0x2C2F33));
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.list = new GuildList(this.loader);
		this.add(Box.createRigidArea(new Dimension(50, 0)));
		this.add(this.list);
		this.add(Box.createRigidArea(new Dimension(50, 0)));
		this.add(this.info = new GuildInfo());
	}
}
