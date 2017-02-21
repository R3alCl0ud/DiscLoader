/**
 * 
 */
package io.discloader.discloader.client.renderer.panel.folders;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import io.discloader.discloader.client.renderer.list.GuildList;
import io.discloader.discloader.common.DiscLoader;

/**
 * @author Perry Berman
 *
 */
public class GuildFolders extends JPanel {

	private static final long serialVersionUID = -1589736360078814907L;
	public final DiscLoader loader;
	public final GuildList list;

	public GuildFolders(DiscLoader loader) {
		super(new GridLayout(1, 3));
		this.loader = loader;
		this.list = new GuildList(this.loader);
		this.add(this.list);
		this.add(new JSeparator(SwingConstants.VERTICAL));
	}
}
