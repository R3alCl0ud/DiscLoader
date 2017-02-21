/**
 * 
 */
package io.discloader.discloader.client.renderer.panel.folders;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

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
		super();
		this.loader = loader;
		this.list = new GuildList(this.loader);
		JSplitPane pane = new JSplitPane();
		pane.setLeftComponent(this.list);
		pane.setMinimumSize(new Dimension(400, 400));
		this.setSize(new Dimension(500, 500));
		this.add(pane);
	}
}
